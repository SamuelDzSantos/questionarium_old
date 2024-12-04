import { AppliedQuestion } from './../../../types/dto/AppliedQuestion';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { AppliedAssessment } from '../../../types/dto/AppliedAssessment';
import { UserData } from '../../../types/dto/UserData';
import { Observable } from 'rxjs';
import { Router, RouterModule } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-avaliacao-aplicadas',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './avaliacao-aplicadas.component.html',
  styleUrl: './avaliacao-aplicadas.component.css'
})
export class AvaliacaoAplicadasComponent implements OnInit {
  user$!: Observable<UserData | null>;
  userId = 0;

  modalEnabled = false;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;


  appliedAssessments = [
    {
      id: 1,
      originalAssessmentId: 1,
      userId: 1,
      institution: 'UFPR',
      department: 'SEPT',
      course: 'TADS',
      classroom: 'IHC',
      professor: 'Sandra',
      instructions: 'Responda as questões com atenção',
      image: '',
      creationDate: '2024-12-01',
      appliedDate: '2024-12-01',
      status: true,
      shuffle: false,
      quantity: 10,
      AppliedQuestion: []
    },
    {
      id: 1,
      originalAssessmentId: 1,
      userId: 1,
      institution: 'UFPR',
      department: 'SEPT',
      course: 'TADS',
      classroom: 'IHC',
      professor: 'Sandra',
      instructions: 'Responda as questões com atenção',
      image: '',
      creationDate: '2024-12-01',
      appliedDate: '2024-12-01',
      status: true,
      shuffle: false,
      quantity: 10,
      AppliedQuestion: []
    }, {
      id: 1,
      originalAssessmentId: 1,
      userId: 1,
      institution: 'UFPR',
      department: 'SEPT',
      course: 'TADS',
      classroom: 'IHC',
      professor: 'Sandra',
      instructions: 'Responda as questões com atenção',
      image: '',
      creationDate: '2024-12-01',
      appliedDate: '2024-12-01',
      status: true,
      shuffle: false,
      quantity: 10,
      AppliedQuestion: []
    }, {
      id: 1,
      originalAssessmentId: 1,
      userId: 1,
      institution: 'UFPR',
      department: 'SEPT',
      course: 'TADS',
      classroom: 'IHC',
      professor: 'Sandra',
      instructions: 'Responda as questões com atenção',
      image: '',
      creationDate: '2024-12-01',
      appliedDate: '2024-12-01',
      status: true,
      shuffle: false,
      quantity: 10,
      AppliedQuestion: []
    },

  ];


  constructor(
    private router: Router,
    private userService: UserService) {

  }
  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
  }

  select_relatorio(id: number) {
    this.router.navigateByUrl("/avaliacao/relatorio", { state: { "id": id } })
  }

}


