import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Route, Router } from '@angular/router';
import { ReportService } from '../../../services/report';
import { AppliedAssessmentReportTs } from '../../../shared/interfaces/applied-assessment-report.ts';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-listar-relatorios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './listar-relatorios.html',
  styleUrl: './listar-relatorios.css'
})




export class ListarRelatoriosComponent implements OnInit {

  reportList$!: Observable<AppliedAssessmentReportTs[]>;

  constructor(
    private router: Router, private reportService: ReportService
  ) { }


  ngOnInit(): void {
    this.reportList$ = this.reportService.getAssessmentReportList();
  }


  relatorios = [
    {
      id: 1,
      data: new Date().toLocaleDateString("pt-br"),
      turma: "DAC 5P 2022",
      disciplina: "DAC",
      geradas: 35,
      corrigidas: 20,
      num_questoes: 20,
      tags: ["Desenvolvimento", "Aplicações", "Corporativas"]
    },
    {
      id: 2,
      data: new Date().toLocaleDateString("pt-br"),
      turma: "DAC 5P 2022",
      disciplina: "DAC",
      geradas: 35,
      corrigidas: 20,
      num_questoes: 20,
      tags: ["Desenvolvimento", "Aplicações", "Corporativas"]
    },
    {
      id: 3,
      data: new Date().toLocaleDateString("pt-br"),
      turma: "DAC 5P 2022",
      disciplina: "DAC",
      geradas: 35,
      corrigidas: 20,
      num_questoes: 20,
      tags: ["Desenvolvimento", "Aplicações", "Corporativas"]
    },
  ];

  viewRelatorio(id: number) {
    this.router.navigate(['/relatorios/', id])
  }

}