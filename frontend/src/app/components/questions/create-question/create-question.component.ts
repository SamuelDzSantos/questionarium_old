import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GerarQuestaoComponent } from '../../../modal/gerar-questao/gerar-questao.component';

@Component({
  selector: 'app-create-question',
  standalone: true,
  imports: [CommonModule, FormsModule, GerarQuestaoComponent],
  templateUrl: './create-question.component.html',
  styleUrl: './create-question.component.css'
})
export class CreateQuestionComponent {

  modalEnabled = false;

  alternativeLabels = ['A', 'B', 'C', 'D', 'E'];

  question = {
    description: '',
    alternatives: [
      { text: '', isCorrect: false },
      { text: '', isCorrect: false },
      { text: '', isCorrect: false },
      { text: '', isCorrect: false },
      { text: '', isCorrect: false }
    ]
  };

  ensureSingleCorrect(selectedIndex: number) {
    this.question.alternatives.forEach((alt, index) => {
      alt.isCorrect = index === selectedIndex;
    });
  }

  generateQuestion() {
    this.mostrarModal();
    console.log('Gerar questão automaticamente');
  }

  saveQuestion() {
    console.log('Salvar questão:');
  }

  cancel() {
    console.log('Ação cancelada');
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }
}
