import { AppliedQuestion } from './../../../types/dto/AppliedQuestion';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AppliedAssessment } from '../../../types/dto/AppliedAssessment';
import { UserData } from '../../../types/dto/UserData';
import { Observable } from 'rxjs';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserInfo } from '../../../interfaces/user/user-info.data';
import { AppliedAssessmentService } from '../../../services/assessment-service/applied-assessment.service';
import { NgxMaskDirective, provideNgxMask } from 'ngx-mask'

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
  data: string = '';

  modalEnabled = false;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;

  searchDescricao = "";
  searchData = "";

  appliedAssessments: AppliedAssessment[] = [];
  filteredAssessments: AppliedAssessment[] = [];

  constructor(
    private router: Router,
    private userService: UserService,
    private appliedAssessmentService: AppliedAssessmentService
  ) { }

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
    this.router.navigateByUrl("/avaliacao/aplicar", { state: { "id": id } })
  }

  search() {

    this.appliedAssessmentService.findWithFilter(this.searchDescricao).subscribe(
      (assessments) => {
        this.filteredAssessments = assessments;
        console.log(this.filteredAssessments);
      }
    );

  }

  onFilterClick() {
    console.log(this.data);
    console.log(this.searchDescricao)
  }

  showReport(assessment: AppliedAssessment) {

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
    this.router.navigateByUrl("/avaliacao/relatorio", { state: { "id": id } })
  }

}


