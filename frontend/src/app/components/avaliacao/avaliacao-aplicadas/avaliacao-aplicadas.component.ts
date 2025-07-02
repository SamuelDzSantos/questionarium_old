import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AppliedAssessment } from '../../../types/dto/AppliedAssessment';
import { UserService } from '../../../services/user.service';
import { AppliedAssessmentService } from '../../../services/assessment-service/applied-assessment.service';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';
import { UserInfo } from '../../../interfaces/user/user-info.data';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-avaliacao-aplicadas',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, NgxMaskDirective],
  providers: [provideNgxMask()],
  templateUrl: './avaliacao-aplicadas.component.html',
  styleUrl: './avaliacao-aplicadas.component.css'
})
export class AvaliacaoAplicadasComponent implements OnInit {

  user$!: Observable<UserInfo | null>;
  userId = 0;
  isAdmin = false;

  searchDescricao = '';
  data = '';
  modalEnabled = false;

  appliedAssessments: AppliedAssessment[] = [];
  filteredAssessments: AppliedAssessment[] = [];

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;

  constructor(
    private router: Router,
    private userService: UserService,
    private appliedAssessmentService: AppliedAssessmentService
  ) {}

   ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe(user => {
      if (user) {
        // Busca apenas avaliações do usuário logado (mude para .getAll se for admin)
        this.appliedAssessmentService.getByUser().subscribe(data => {
          this.appliedAssessments = data;
          this.filteredAssessments = data;
        });
      }
    });
  }

  select_assessment(id: number) {
    this.router.navigateByUrl("/avaliacao/aplicar", { state: { "id": id } });
  }

  private formatDate(dateStr: string): string | undefined {
    if (!dateStr || dateStr.length !== 8) return undefined;

    const dd = dateStr.substring(0, 2);
    const mm = dateStr.substring(2, 4);
    const yyyy = dateStr.substring(4, 8);

    if (isNaN(+dd) || isNaN(+mm) || isNaN(+yyyy)) return undefined;

    return `${yyyy}-${mm.padStart(2, '0')}-${dd.padStart(2, '0')}`;
  }


  search() {
    const formattedDate = this.formatDate(this.data) ?? undefined;

    this.appliedAssessmentService.findWithFilter(this.searchDescricao, formattedDate).subscribe(
      (assessments) => {
        this.filteredAssessments = assessments;
      },
      (error) => {
        console.error('Erro ao buscar avaliações com filtro:', error);
      }
    );
  }

  onFilterClick() {
    this.search();
  }

  showReport(assessment: AppliedAssessment) {
    this.router.navigate(['/relatorios/', assessment.id]);
  }

  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }

  select_relatorio(id: number) {
    this.router.navigateByUrl("/avaliacao/relatorio", { state: { "id": id } });
  }
}
