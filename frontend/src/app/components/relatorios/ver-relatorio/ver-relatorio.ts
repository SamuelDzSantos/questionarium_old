import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ver-relatorio',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ver-relatorio.html',
  styleUrl: './ver-relatorio.css'
})
export class VerRelatorioComponent {

  constructor(private router: Router) { }

  relatorio =
    {
      id: 1,
      data: new Date().toLocaleDateString("pt-br"),
      hora: "19:00 - 20:00",
      turma: "DAC 5P 2022",
      disciplina: "DAC",
      geradas: 35,
      corrigidas: 20,
      nao_corrigidas: 15,
      num_questoes: 20,
      tags: ["Desenvolvimento", "Aplicações", "Corporativas"],
      aplicacoes: [
        {
          codigo: "A0001.1",
          identificador: "Aluno 1",
          acertos: 17,
          erros: 3,
          status: "CORRIGIDO"
        },
        {
          codigo: "A0001.2",
          identificador: "Aluno 2",
          acertos: 15,
          erros: 5,
          status: "CORRIGIDO"
        },
        {
          codigo: "A0001.3",
          identificador: "Aluno 3",
          status: "NÃO CORRIGIDO"
        },
        {
          codigo: "A0001.4",
          identificador: "Aluno 4",
          status: "NÃO CORRIGIDO"
        },
        {
          codigo: "A0001.5",
          identificador: "Aluno 5",
          status: "NÃO CORRIGIDO"
        },
      ]
    }

  cancel() {
    this.router.navigate(["relatorios"])
  }

}