import os
import requests
from flask import Flask, request, jsonify
from langchain_core.messages import HumanMessage, SystemMessage
from custom_chatbot import CustomKnowledgeBot

app = Flask(__name__)

# Custom chatbot setup (unchanged)
data_path = r"data.txt"
knowledge_file_path = os.path.join(os.path.dirname(__file__), data_path)
chatbot = CustomKnowledgeBot(custom_knowledge_path=knowledge_file_path)

@app.route("/hi")
def home():
    return "Hello, World!"

@app.route("/ai/chat", methods=['POST'])
def chat():
    try:
        data = request.json
        user_message = data.get('message', '')
        language = data.get('language', 'Brazilian Portuguese')

        messages = [
            SystemMessage(content="You are a helpful website assistant"),
            HumanMessage(content=user_message)
        ]

        response = chatbot.run_chat(messages, language)

        return jsonify({ 'response': response })

    except Exception as e:
        return jsonify({ 'error': str(e) }), 500

OPENAI_API_KEY = os.getenv("OPENAI_API_KEY")
OPENAI_API_URL = "https://api.openai.com/v1/chat/completions"

@app.route("/ai/openai", methods=["POST"])
def call_openai():
    try:
        data = request.get_json()
        prompt = data.get("prompt", "")

        payload = {
            "model": "gpt-3.5-turbo",
            "messages": [{"role": "user", "content": prompt}],
            "max_tokens": 1000,
            "temperature": 0.7
        }

        headers = {
            "Authorization": f"Bearer {OPENAI_API_KEY}",
            "Content-Type": "application/json"
        }

        response = requests.post(OPENAI_API_URL, json=payload, headers=headers)
        response.raise_for_status()

        result = response.json()
        message = result["choices"][0]["message"]["content"]

        return jsonify({ "response": message })

    except requests.exceptions.RequestException as e:
        return jsonify({ "error": str(e) }), 500
    except Exception as e:
        return jsonify({ "error": str(e) }), 500


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=14011, debug=True)
