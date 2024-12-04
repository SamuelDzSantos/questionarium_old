import { Component, EventEmitter, Output } from '@angular/core';
import { OpenAIService } from '../../services/openai-service/openai-service.service';
import { FormsModule } from '@angular/forms';
import { CreateQuestionComponent } from '../../components/questions';

@Component({
  selector: 'app-gerar-questao-modal',
  templateUrl: './gerar-questao.component.html',
  styleUrls: ['./gerar-questao.component.css'],
  standalone: true,
  imports: [FormsModule],
})
export class GerarQuestaoComponent {
  disciplines: string[] = ['Matemática', 'Português', 'História']; // Exemplos
  questionTypes: string[] = ['Objetiva', 'Dissertativa'];
  subjects: string[] = [];
  selectedDiscipline: string = '';
  selectedQuestionType: string = '';
  selectedSubject: string = '';
  generatedQuestion: string = '';

  constructor(private openAIService: OpenAIService) {}

  @Output() closeModalEvent = new EventEmitter<void>();

  niveis = [
    { label: 'ENSINO_FUNDAMENTAL', value: 0 },
    { label: 'ENSINO_MÉDIO', value: 1 },
    { label: 'ENSINO_SUPERIOR', value: 2 }
  ];

  categorias = [
    { label: 'MATEMÁTICA', value: 0 },
    { label: 'PORTUGUÊS', value: 1 },
    { label: 'FÍSICA', value: 2 }
  ];

  selectedNivel: number | null = null;
  selectedCategoria: number | null = null;
  discursiva: boolean = false;

  generateQuestion() {
    const prompt = `Gere uma questão de ${this.selectedQuestionType} para a disciplina ${this.selectedDiscipline} sobre ${this.selectedSubject}.`;
    this.openAIService.generateText(prompt).subscribe((response) => {
      this.generatedQuestion = response.data;
    });
  }

  acceptQuestion() {
    alert('Questão aceita!');
  }

  generateAnother() {
    this.generatedQuestion = '';
  }

  cancel() {
    this.closeModalEvent.emit()
  }

  closeModal() {
    this.closeModalEvent.emit()
  }
}
