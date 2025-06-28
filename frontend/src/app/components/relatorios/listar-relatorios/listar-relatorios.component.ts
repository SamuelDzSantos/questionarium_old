import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Route, Router } from '@angular/router';

@Component({
  selector: 'app-listar-relatorios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listar-relatorios.component.html',
  styleUrl: './listar-relatorios.component.css'
})

export class ListarRelatoriosComponent {

  constructor(
    private router:Router
  ){}

  relatorios = [
  {
    id: 1,
    data: new Date().toLocaleDateString("pt-br"),
    turma: "DAC 5P 2022",
    disciplina: "DAC",
    geradas: 35,
    corrigidas: 20,
    num_questoes: 20,
    tags: ["Desenvolvimento","Aplicações","Corporativas"]
  },
  {
    id: 2,
    data: new Date().toLocaleDateString("pt-br"),
    turma: "DAC 5P 2022",
    disciplina: "DAC",
    geradas: 35,
    corrigidas: 20,
    num_questoes: 20,
    tags: ["Desenvolvimento","Aplicações","Corporativas"]
  },
  {
    id: 3,
    data: new Date().toLocaleDateString("pt-br"),
    turma: "DAC 5P 2022",
    disciplina: "DAC",
    geradas: 35,
    corrigidas: 20,
    num_questoes: 20,
    tags: ["Desenvolvimento","Aplicações","Corporativas"]
  },
];

  viewRelatorio(id:number){
    this.router.navigate(['/relatorios/', id])
  }

}
