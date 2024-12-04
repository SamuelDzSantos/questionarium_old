import { Component } from '@angular/core';
import { AssessmentHeader } from '../../../types/dto/AssessmentHeader';
import { AssessmentHeaderService } from '../../../services/assessment-header.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CriarCabecalhoComponent } from '../../../modal/criar-cabecalho/criar-cabecalho.component';
import { ConsultarCabecalhoComponent } from '../../../modal/consultar-cabecalho/consultar-cabecalho.component';

interface question {
  instituicao: string,
  enunciado: string,
  tipo: string
}



@Component({
  selector: 'app-avaliacao-criar',
  standalone: true,
  imports: [FormsModule,CommonModule, CriarCabecalhoComponent, ConsultarCabecalhoComponent],
  templateUrl: './avaliacao-criar.component.html',
  styleUrl: './avaliacao-criar.component.css'
})
export class AvaliacaoCriarComponent {
  [x: string]: any;
  
  modalCreateCabecalhoEnabled = false;
  modalGetCabecalhoEnabled = false;

  header: AssessmentHeader = {
    id: 0,
    institution: '',
    department: '',
    course: '',
    classroom: '',
    professor: '',
    instructions: '',
    userId: 0,
  };

  questoes: question[] = [{
    instituicao: "InstituiçãoXX",
    enunciado: "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Illum ab consequuntur enim    repellat provident, officiis fugit est quis veritatis ducimus quidem expedita voluptatem neque error blanditiis odio esse suscipit",
    tipo: "Pública"
  }, {
    instituicao: "InstituiçãoXX",
    enunciado: "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Illum ab consequuntur enim    repellat provident, officiis fugit est quis veritatis ducimus quidem expedita voluptatem neque error blanditiis odio esse suscipit",
    tipo: "Pública"
  }, {
    instituicao: "InstituiçãoXX",
    enunciado: "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Illum ab consequuntur enim    repellat provident, officiis fugit est quis veritatis ducimus quidem expedita voluptatem neque error blanditiis odio esse suscipit",
    tipo: "Pública"
  }, {
    instituicao: "InstituiçãoXX",
    enunciado: "Lorem ipsum dolor sit, amet consectetur adipisicing elit. Illum ab consequuntur enim    repellat provident, officiis fugit est quis veritatis ducimus quidem expedita voluptatem neque error blanditiis odio esse suscipit",
    tipo: "Pública"
  }]

  pad(num: string, size: number) {
    let s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
  }

  createHeader() {
    this.mostrarModalCreate();
  }

  getHeaders() {
    this.mostrarModal();
  }

  mostrarModalCreate() {
    this.modalCreateCabecalhoEnabled = true;
  }

  mostrarModal() {
    this.modalGetCabecalhoEnabled = true;
  }

  setHeader(header:AssessmentHeader){
    this.header = header;
  }

  constructor(private headerService: AssessmentHeaderService) { }

  saveHeader(): void {
    this.headerService.createHeader(this.header).subscribe({
      next: (response) => {
        console.log('Cabeçalho salvo:', response);
      },
      error: (err) => {
        console.error('Erro ao salvar cabeçalho:', err);
      },
    });
  }

  loadHeader(): void{
    console.log("loadHeader")
  }

  public fecharCreateCabecalhoModal() {
    this.modalCreateCabecalhoEnabled = false;
  }

  public fecharGetCabecalhoModal() {
    this.modalGetCabecalhoEnabled = false;
  }

}
