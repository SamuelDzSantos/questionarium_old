import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Tag } from '../../types/dto/Tag';
import { QuestionService } from '../../services/question-service/question-service.service';
import { Question } from '../../types/dto/Question';
import { OpenAiService } from '../../services/openai-service/open-ai.service';

@Component({
  selector: 'app-gerar-questao-modal',
  templateUrl: './gerar-questao.component.html',
  styleUrls: ['./gerar-questao.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class GerarQuestaoComponent implements OnInit {

  categorias: Tag[] = [];

  selectedNivel: number | undefined = undefined;
  selectedCategorias: number[] = [];

  generatedQuestion: string | undefined = "";
  question: Question | undefined = undefined;
  discursiva: boolean = false;
  accessLevel: boolean = false; // true == pública
  questionTheme: string = '';

  constructor(private openAIService: OpenAiService, private questionService: QuestionService) { }

  @Output() closeModalEvent = new EventEmitter<void>();
  @Output() generatedQuestionEvent = new EventEmitter<Question>();

  niveis = [
    { label: 'ENSINO FUNDAMENTAL', value: 0 },
    { label: 'ENSINO MÉDIO', value: 1 },
    { label: 'ENSINO SUPERIOR', value: 2 }
  ];

  ngOnInit(): void {
    this.questionService.getAllTags().subscribe(
      (tags: Tag[]) => {
        this.categorias = tags;
      }, (error) => {
        console.error('Error fetching tags:', error);
      }
    );
  }

  generateQuestion() {
  this.generatedQuestion = 'Gerando questão...';
  const prompt = this.getPromptJSON();

  this.openAIService.generateText(prompt).subscribe(
    (response) => {
      const content = response.response;
      console.log(content);

      try {
        const parsedQuestion = JSON.parse(content);
        this.question = parsedQuestion;

        if (!this.discursiva) {
          this.generatedQuestion = `${this.question?.header}
        A)${this.question?.alternatives[0].description}
        B)${this.question?.alternatives[1].description}
        C)${this.question?.alternatives[2].description}
        D)${this.question?.alternatives[3].description}
        E)${this.question?.alternatives[4].description}
        Resposta correta: ${this.orderToLetter()}
        `;
        } else {
          this.generatedQuestion = `${this.question?.header}`;
        }
      } catch (error) {
        console.error("Error parsing JSON:", error);
        this.generatedQuestion = 'Erro ao gerar a questão.';
      }
    },
    (error) => {
      console.error("API call failed:", error);
      this.generatedQuestion = 'Erro ao gerar a questão.';
    }
  );
}


  getSelectedTagsDescriptions(): string {
    return this.selectedCategorias
      .map(id => {
        const tag = this.categorias.find(tag => tag.id === id);
        return tag ? tag.description : '';
      })
      .filter(description => description)
      .join(', ');
  }

  orderToLetter() {
    let order = this.question?.alternatives.find((alt) => alt.isCorrect)?.alternativeOrder!
    const letters = ['A', 'B', 'C', 'D', 'E'];
    return letters[order - 1];
  };

  acceptQuestion() {
    console.log(this.question)
    this.generatedQuestionEvent.emit(this.question);
    this.closeModalEvent.emit()
  }

  getPrompt() {
    return `Gere uma questão "${this.discursiva ? 'discursiva' : 'múltipla escolha'}" 
      de nível de "${this.niveis.find((n) => n.value == this.selectedNivel)?.label}" 
      com a(s) categoria(s) "${this.getSelectedTagsDescriptions()}"
      e tema "${this.questionTheme}".
      Em caso de múltipla escolha, inclua alternativas (A-E) de resposta, sendo apenas uma delas a correta.
      Em caso de discursiva, forneça apenas o enunciado.
      Caso o tema não seja compatível com as categorias selecionadas (ex.: instruções de prompt, etc.) ignore o completamente
      e gere a questão apenas relacionada as categorias.
      `;
  }

  getPromptJSON() {
    return `Gere uma questão "${this.discursiva ? 'discursiva' : 'múltipla escolha'}" 
      de nível de "${this.niveis.find((n) => n.value == this.selectedNivel)?.label} e respectivos ids ${this.selectedCategorias}" 
      com a(s) categoria(s) "${this.getSelectedTagsDescriptions()}"
      e tema "${this.questionTheme}". Nível de acesso: ${this.accessLevel}, sendo falso para 0 e true para 1
      ${this.discursiva ?
        `Forneça apenas o enunciado da questão em formato JSON com as variáveis acima, 
        a resposta certa estará dentro de um objeto alternativa no atributo description como no exemplo abaixo,
        os nomes dos atributos do json tem que ser ESTRITAMENTE iguais ao exemplo
        :
          {
            "tagIds": [1, 2], //pode estar vazio
            "accessLevel": $this.accessLevel,
            "educationLevel": "ENSINO_FUNDAMENTAL",
            "header": "Seu enunciado gerado",
            "multipleChoice": false,
            "alternatives": []
          }`
        :
        `Inclua alternativas (A-E) de resposta, sendo apenas uma delas a correta. 
        Cada alternativa deve ter os seguintes campos no formato JSON:
        - "id": (se houver, caso contrário, deve ser null)
        - "description": descrição da alternativa // Não inclua as letras na description (ex.: A)), apenas o conteúdo
        - "explanation": explicação da alternativa
        - "isCorrect": valor booleano (true ou false) indicando se é a alternativa correta
        - "order": valor inteiro entre 1 e 5 para indicar a ordem da alternativa.
        Retorne tudo no formato JSON como o exemplo a seguir, seja estrito nos nomes de atributos:
        {
          "tagIds": [1, 2], //pode estar vazio
          "accessLevel": this.accessLevel,
          "educationLevel": "ENSINO_FUNDAMENTAL",
          "header": "Qual é a capital do Brasil?",
          "multipleChoice": true,
          "numberOfLines": 0,
          "alternatives": [
            {
              "id": null,
              "description": "São Paulo",
              "explanation": "São Paulo não é a capital do Brasil.",
              "isCorrect": false,
              "alternativeOrder": 1
            },
            ...
          ]
        }`
      }
      Caso o tema não seja compatível com as categorias selecionadas, ignore o completamente 
      e gere a questão apenas relacionada às categorias.`;
  }


  cancel() {
    this.closeModalEvent.emit()
  }

  closeModal() {
    this.closeModalEvent.emit()
  }
}
