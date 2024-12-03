import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-question',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-question.component.html',
  styleUrl: './create-question.component.css'
})
export class CreateQuestionComponent {

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
    console.log('Gerar questão automaticamente');
  }

  saveQuestion() {
    console.log('Salvar questão:');
  }

  cancel() {
    console.log('Ação cancelada');
  }
}
