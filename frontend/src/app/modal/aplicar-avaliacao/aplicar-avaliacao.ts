import { FormsModule } from '@angular/forms';
import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-aplicar-avaliacao',
  imports: [FormsModule],
  templateUrl: './aplicar-avaliacao.html',
  styleUrl: './aplicar-avaliacao.css'
})
export class AplicarAvaliacaoComponent {
  @Input() modelId!: number;

  @Output() applied = new EventEmitter<any>();
  @Output() cancelled = new EventEmitter<void>();

  form = {
    description: '',
    applicationDate: '',
    quantity: 1,
    shuffleQuestions: false
  };

  closeModal() {
    this.cancelled.emit();
  }

  applyAssessment() {
    console.log("OPa")
    if (!this.form.applicationDate || !this.form.quantity || !this.modelId || !this.form.description) {
      console.log(this.modelId)
      console.log(this.form.description)
      console.log(this.form.applicationDate)
      console.log(this.form.quantity)
      console.log("erro")
      return;
    }

    const payload = {
      modelId: this.modelId,
      description: this.form.description,
      quantity: Number(this.form.quantity),
      applicationDate: this.form.applicationDate,
      shuffleQuestions: this.form.shuffleQuestions
    };


    this.applied.emit(payload);
  }
}