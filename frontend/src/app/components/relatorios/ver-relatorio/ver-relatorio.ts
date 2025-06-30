import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { AppliedReport } from '../../../shared/interfaces/applied-report';
import { ReportService } from '../../../services/report';

@Component({
  selector: 'app-ver-relatorio',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './ver-relatorio.html',
  styleUrl: './ver-relatorio.css'
})
export class VerRelatorioComponent implements OnInit {

  id: number;
  relatorio!: AppliedReport;
  tags: string[] = [];
  constructor(private router: Router, private reportService: ReportService) {

    const root = this.router.routerState.snapshot.root;
    const routeParam = this.getRouteParam(root, 'id');
    if (routeParam == null) {
      this.id = 0
    } else {
      this.id = Number(routeParam);
    }
  }

  ngOnInit(): void {
    this.reportService.getReportData(this.id).subscribe((r) => {
      this.relatorio = r;
      for (let i = 0; i < this.relatorio.tags.length; i++) {
        this.reportService.getTag(this.relatorio.tags[i]).subscribe((tag) => {
          this.tags.push(tag.name)
        });

      }
    });


  }

  getRouteParam(route: ActivatedRouteSnapshot, key: string): string | null {
    if (route.paramMap.has(key)) {
      return route.paramMap.get(key);
    }
    for (const child of route.children) {
      const value = this.getRouteParam(child, key);
      if (value !== null) {
        return value;
      }
    }
    return null;
  }
  /*
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
  */
  cancel() {
    this.router.navigate(["relatorios"])
  }

}