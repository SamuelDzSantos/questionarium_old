import os
import sys
import traceback
from typing import Sequence
from typing_extensions import Annotated, TypedDict

from langchain_core.messages import (
    HumanMessage, BaseMessage, AIMessage, SystemMessage
)
from langchain_core.prompts import ChatPromptTemplate, MessagesPlaceholder
from langchain_openai import ChatOpenAI
from langchain_core.documents import Document
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_openai import OpenAIEmbeddings
from langchain_community.vectorstores import FAISS

from langgraph.checkpoint.memory import MemorySaver
from langgraph.graph import START, StateGraph, add_messages
from dotenv import load_dotenv
load_dotenv()


class State(TypedDict):
    messages: Annotated[Sequence[BaseMessage], add_messages]
    language: str


class CustomKnowledgeBot:
    def __init__(self, model=None, custom_knowledge_path: str = None):
        print("custom_knowledge_bot")
        self.api_key = os.getenv("OPENAI_API_KEY")
        if not self.api_key:
            print("ERROR: OpenAI API key not found. Set OPENAI_API_KEY environment variable.")
            sys.exit(1)

        try:
            self.model = model or ChatOpenAI(
                model="gpt-3.5-turbo",
                openai_api_key=self.api_key
            )
        except Exception as e:
            print(f"Error initializing model: {e}")
            sys.exit(1)

        self.vector_store = None
        if custom_knowledge_path:
            try:
                self.setup_knowledge_base(custom_knowledge_path)
            except Exception as e:
                print(f"Error setting up knowledge base: {e}")
                traceback.print_exc()
                sys.exit(1)

        # Prompt template
        self.prompt_template = ChatPromptTemplate.from_messages([
            (
                "system",
                "You are a helpful assistant. Answer all questions to the best of your ability in {language}. "
                "If the question cannot be answered from your knowledge base, clearly state that you don't have enough information"
                "and refer the user to the contact questionarium@email.com"
            ),
            MessagesPlaceholder(variable_name="messages"),
        ])

    def setup_knowledge_base(self, knowledge_file_path: str):
        print("setup_knowledge_base")
        with open(knowledge_file_path, 'r', encoding='utf-8') as file:
            text = file.read()

        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200,
            length_function=len
        )
        texts = text_splitter.split_text(text)

        documents = [Document(page_content=t) for t in texts]

        embeddings = OpenAIEmbeddings(openai_api_key=self.api_key)

        self.vector_store = FAISS.from_documents(documents, embeddings)

    def run_chat(self, messages: Sequence[BaseMessage], language: str = "Brazilian Portuguese"):

        try:
            app = self.create_chat_workflow()

            config = {"configurable": {"thread_id": "custom_knowledge_chat"}}

            state_query = {
                "messages": messages,
                "language": language
            }

            responses = []

            for event in app.stream(state_query, config):

                if isinstance(event, dict) and 'model' in event:
                    for msg in event['model']['messages']:
                        if isinstance(msg, AIMessage):
                            responses.append(msg.content)

            final_response = responses[-1] if responses else "I'm unable to respond."
            return final_response

        except Exception as e:
            print(f"Error in chat workflow: {e}")
            traceback.print_exc()
            return "An error occurred during chat."

    def create_chat_workflow(self):

        def call_model(state: State):

            current_messages = state["messages"]

            latest_query = current_messages[-1].content if current_messages else ""

            context = self.retrieve_relevant_context(latest_query)

            full_prompt_context = (
                f"Relevant Context: {context}\n\n"
                f"Use the above context to answer the following query. "
                f"If the context does not provide enough information, "
                f"clearly state that you don't know the answer."
            )

            modified_messages = current_messages[:-1] + [
                HumanMessage(content=full_prompt_context + "\n\n" + latest_query)
            ]

            prompt = self.prompt_template.invoke({
                "messages": modified_messages,
                "language": state.get("language", "English")
            })

            response = self.model.invoke(prompt)

            return {"messages": [response]}

        workflow = StateGraph(state_schema=State)
        workflow.add_edge(START, "model")
        workflow.add_node("model", call_model)

        memory = MemorySaver()
        return workflow.compile(checkpointer=memory)

    def retrieve_relevant_context(self, query: str, top_k: int = 3) -> str:

        if not self.vector_store:
            print("No vector store available")
            return ""

        try:
            relevant_docs = self.vector_store.similarity_search(query, k=top_k)

            context = "\n\n".join([doc.page_content for doc in relevant_docs])
            return context
        except Exception as e:
            print(f"Error retrieving context: {e}")
            return ""


def main():
    knowledge_file_path = os.path.join(os.path.dirname(__file__), "data.txt")
    print("main")
    try:
        bot = CustomKnowledgeBot(
            custom_knowledge_path=knowledge_file_path
        )

        messages = [
            SystemMessage(content="You are a helpful website assistant"),
            HumanMessage(content="Como adicionar uma nova questão no site?")
        ]

        response = bot.run_chat(messages)
        print(response)

    except FileNotFoundError:
        print(f"Knowledge file not found at {knowledge_file_path}. Please create the file.")
    except Exception as e:
        print(f"An error occurred: {e}")
        traceback.print_exc()


if __name__ == "__main__":
    main()