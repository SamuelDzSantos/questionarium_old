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

@Component({
  selector: 'app-avaliacao',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, CriarCabecalhoComponent],
  templateUrl: './avaliacao.component.html',
  styleUrl: './avaliacao.component.css'
})
export class AvaliacaoComponent implements OnInit {
  user$!: Observable<UserInfo | null>;
  searchValue = "";
  assessments: AssessmentModel[] = [];
  filteredAssessments: AssessmentModel[] = [];
  modalEnabled = false;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;

  constructor(
    private router: Router,
    private userService: UserService,
    private assessmentModelService: AssessmentModelService
  ) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe(user => {
      if (user) {
        // Busca apenas avaliações do usuário logado (mude para .getAll se for admin)
        this.assessmentModelService.getByUser(user.id, false).subscribe(data => {
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
    if (this.searchValue?.trim()) {
      const value = this.searchValue.toLowerCase();
      this.filteredAssessments = this.assessments.filter(a =>
        (a.course ?? '').toLowerCase().includes(value) ||
        (a.department ?? '').toLowerCase().includes(value) ||
        (a.institution ?? '').toLowerCase().includes(value)
      );
    } else {
      this.filteredAssessments = this.assessments;
    }
  }

  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }

  updateAssessment(assessment: AssessmentModel) {
    this.router.navigate(['/avaliacao/editar', assessment.id]);
  }

  applyAssessment(assessment: AssessmentModel) {
    this.router.navigate(['/avaliacao/aplicar'], { state: { id: assessment.id } });
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
}