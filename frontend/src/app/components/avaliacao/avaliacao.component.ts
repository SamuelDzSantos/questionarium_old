import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../services/user.service';
import { UserData } from '../../types/dto/UserData';
import { Observable } from 'rxjs';
import { AssessmentModelService } from '../../services/assessment-service/assessment-model.service';
import { AssessmentModel } from '../../types/dto/AssessmentModel';
import { CriarCabecalhoComponent } from '../../modal/criar-cabecalho/criar-cabecalho.component';
import { UserInfo } from '../../interfaces/user/user-info.data';
import { AppliedAssessmentService } from '../../services/assessment-service/applied-assessment.service';
import { AplicarAvaliacao } from '../../modal/aplicar-avaliacao/aplicar-avaliacao';

@Component({
  selector: 'app-avaliacao',
  standalone: true,
  imports: [
    RouterModule,
    CommonModule,
    FormsModule,
    CriarCabecalhoComponent,
    AplicarAvaliacao
  ],
  templateUrl: './avaliacao.component.html',
  styleUrl: './avaliacao.component.css'
})
export class AvaliacaoComponent implements OnInit {
  user$!: Observable<UserInfo | null>;
  searchDescricao = "";
  searchInstituicao = "";
  searchDisciplina = "";
  searchClasse = "";
  assessments: AssessmentModel[] = [];
  filteredAssessments: AssessmentModel[] = [];
  selectedAssessment: AssessmentModel | null = null;
  modalEnabled = false;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;

  constructor(
    private router: Router,
    private userService: UserService,
    private assessmentModelService: AssessmentModelService,
    private appliedAssessmentService: AppliedAssessmentService // <--- FALTAVA AQUI
  ) { }


  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe(user => {
      if (user) {

        // Busca apenas avaliações do usuário logado (mude para .getAll se for admin)
        this.assessmentModelService.getByUser().subscribe(data => {
          this.assessments = data;
          this.filteredAssessments = data;
        });
      }
    });
  }

  select_assessment(id: number) {
    this.router.navigateByUrl("/avaliacao/aplicar", { state: { "id": id } })
  }

  search() {

    this.assessmentModelService.findWithFilter(this.searchDescricao, this.searchInstituicao, this.searchClasse, this.searchDisciplina).subscribe(
      (assessments) => {
        this.filteredAssessments = assessments;
        console.log(this.filteredAssessments);
      }
    );

    /*
        if (this.searchDescricao?.trim()) {
          const value = this.searchDescricao.toLowerCase();
          this.filteredAssessments = this.assessments.filter(a =>
            (a.course ?? '').toLowerCase().includes(value) ||
            (a.department ?? '').toLowerCase().includes(value) ||
            (a.institution ?? '').toLowerCase().includes(value)
          );
        } else {
          this.filteredAssessments = this.assessments;
        }
        */
  }

  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }

  updateAssessment(assessment: AssessmentModel) {
    this.router.navigate(['/avaliacao/editar', assessment.id]);
  }

  // Para abrir o modal com a avaliação escolhida
  applyAssessment(assessment: AssessmentModel) {
    this.selectedAssessment = assessment;
    this.modalEnabled = true;
  }

  deleteAssessment(assessment: AssessmentModel) {
    /*
    if (confirm('Tem certeza que deseja deletar esta avaliação?')) {
      this.assessmentModelService.delete(assessment.id).subscribe(() => {
        this.assessments = this.assessments.filter(a => a.id !== assessment.id);
        this.filteredAssessments = this.filteredAssessments.filter(a => a.id !== assessment.id);
      });
    }*/
  }

  // Quando o modal disparar o applyEvent (com dados preenchidos pelo usuário)
  onApplyAssessment(formData: { date: string; count: number; shuffle: boolean }) {
    if (!this.selectedAssessment) return;

    // DTO para o backend
    const payload = {
      modelId: this.selectedAssessment.id,
      quantity: Number(formData.count),
      applicationDate: formData.date, // string "YYYY-MM-DD"
      shuffleQuestions: formData.shuffle,
    };
    // user (observable user$ ou localstorage)
    this.user$.subscribe(user => {
      if (!user) return;
      const isAdmin = (user as any).isAdmin || false;
      this.appliedAssessmentService.apply(payload, user.id, isAdmin).subscribe({
        next: (response) => {
          this.fecharModal();
          this.router.navigate(['/avaliacao/aplicadas']);
        },
        error: (err) => {
          alert('Erro ao aplicar avaliação!');
        }
      });
    });
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

  // Para fechar o modal
  fecharModal() {
    this.modalEnabled = false;
    this.selectedAssessment = null;
  }
}