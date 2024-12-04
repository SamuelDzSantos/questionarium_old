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

  loadSubjects() {
    // Carregar assuntos com base na disciplina selecionada
    this.subjects = ['Assunto 1', 'Assunto 2', 'Assunto 3']; // Exemplos
  }

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
