import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-aplicar-avaliacao',
  templateUrl: './aplicar-avaliacao.html',
  styleUrl: './aplicar-avaliacao.css',
  standalone: true,
  imports: [FormsModule]
})
export class AplicarAvaliacao {
  @Output() closeModalEvent = new EventEmitter<void>();
  @Output() applyEvent = new EventEmitter<{ date: string; count: number; shuffle: boolean }>();

  date = '';
  count = 1;
  shuffle = false;

  closeModal() {
    this.closeModalEvent.emit();
  }

  applyAssessment() {
    this.applyEvent.emit({
      date: this.date,
      count: this.count,
      shuffle: this.shuffle,
    });
  }

}
