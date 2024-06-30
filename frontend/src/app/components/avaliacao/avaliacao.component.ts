import { Component, ElementRef, ViewChild } from '@angular/core';

@Component({
  selector: 'app-avaliacao',
  standalone: true,
  imports: [],
  templateUrl: './avaliacao.component.html',
  styleUrl: './avaliacao.component.css'
})
export class AvaliacaoComponent {

@ViewChild('titulo') titulo! : ElementRef<HTMLElement>;

  montar(){
      this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }

}
