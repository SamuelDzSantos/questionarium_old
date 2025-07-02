import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { UserInfo } from '../../interfaces/user/user-info.data';
import { CriarCabecalhoComponent } from '../../modal/criar-cabecalho/criar-cabecalho.component';
import { AssessmentModelService } from '../../services/assessment-service/assessment-model.service';
import { UserService } from '../../services/user.service';
import { AssessmentModel } from '../../types/dto/AssessmentModel';
import { AppliedAssessmentService } from '../../services/assessment-service/applied-assessment.service';
import { AplicarAvaliacaoComponent } from '../../modal/aplicar-avaliacao/aplicar-avaliacao';

@Component({
  selector: 'app-avaliacao',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, AplicarAvaliacaoComponent],
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

  modalEnabled = false;
  selectedModelId: number = 0;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;

  constructor(
    private router: Router,
    private userService: UserService,
    private assessmentModelService: AssessmentModelService,
    private appliedAssessmentService: AppliedAssessmentService,

  ) { }

  ngOnInit(): void {



    this.user$ = this.userService.getCurrentUser();
    this.getAssessments();
  }




  private getAssessments() {
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

  onAplicarAssessment(payload: any) {
    console.log("hi")

    this.appliedAssessmentService.apply(payload).subscribe({
      next: () => {
        this.fecharModal();
      },
      error: () => {
        alert('Erro ao aplicar avaliação');
      }
    });
  }

  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }

  updateAssessment(assessment: AssessmentModel) {
    this.router.navigate(['/avaliacao', assessment.id]);
  }

  applyAssessment(assessment: AssessmentModel) {
    this.selectedModelId = assessment.id
    this.modalEnabled = true;
  }

  deleteAssessment(assessment: AssessmentModel) {

    let id: number = assessment.id;

    this.assessmentModelService.delete(id).subscribe(() => {
      this.getAssessments()
    }
    );

    /*
    if (confirm('Tem certeza que deseja deletar esta avaliação?')) {
      this.assessmentModelService.delete(assessment.id).subscribe(() => {
        this.assessments = this.assessments.filter(a => a.id !== assessment.id);
        this.filteredAssessments = this.filteredAssessments.filter(a => a.id !== assessment.id);
      });
    }*/
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
    this.selectedModelId = 0;
  }

}