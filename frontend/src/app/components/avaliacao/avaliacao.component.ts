import { Component, ElementRef, ViewChild } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AssessmentHeaderService } from '../../services/assessment-header.service';
import { AssessmentHeader } from '../../types/dto/AssessmentHeader';

@Component({
  selector: 'app-avaliacao',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './avaliacao.component.html',
  styleUrl: './avaliacao.component.css'
})
export class AvaliacaoComponent {

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;
  
  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }



}
