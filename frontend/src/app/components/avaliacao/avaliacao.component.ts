import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { CriarCabecalhoComponent } from '../../modal/criar-cabecalho/criar-cabecalho.component';

@Component({
  selector: 'app-avaliacao',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, CriarCabecalhoComponent],
  templateUrl: './avaliacao.component.html',
  styleUrl: './avaliacao.component.css'
})
export class AvaliacaoComponent {

  modalEnabled = false;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;
  
  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }


}
