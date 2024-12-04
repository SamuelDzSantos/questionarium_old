import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, switchMap } from 'rxjs';
import { UserService } from '../../../services/user.service';
import { UserData } from '../../../types/dto/UserData';
import { Question } from '../../../types/dto/Question';
import { Alternative } from '../../../types/dto/Alternative';
import { QuestionHeader } from '../../../types/dto/QuestionHeader';

export interface Assessment {
  id: number,
  questions: number[],
  creationDate: string,
  headerId: number
}

export interface Header {
  id: number,
  department: string,
  institution: string,
  course: string,
  classroom: string,
  professor: string,
  instructions: string,
  creationDate: string
}


@Component({
  selector: 'app-avaliacao-aplicar',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './avaliacao-aplicar.component.html',
  styleUrl: './avaliacao-aplicar.component.css'
})
export class AvaliacaoAplicarComponent {

  appliesAssessment = {
    applicationDate: '',
    quantity: 1,
    shuffle: false
  };

  minDate: string;
  maxDate: string;

  header: Header = {
    id: 0,
    department: '',
    institution: '',
    course: '',
    classroom: '',
    professor: '',
    instructions: '',
    creationDate: ''
  }

  assessment: Assessment = {
    id: 0,
    creationDate: "2024-08-12",
    headerId: this.header.id,
    questions: [],
  };

  state$!: Observable<object>
  assessmentId = 0
  user$!: Observable<UserData | null>;
  userId = 0;

  apiUrlAssessment = 'http://localhost:14005/assessments/';
  apiUrlHeader = 'http://localhost:14005/header/';
  apiUrlAppliedAssessment = 'http://localhost:14005/';

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private userService: UserService) {

    let navigation = this.router.getCurrentNavigation();
    if (navigation?.extras.state != null && navigation?.extras.state != undefined) {
      this.assessmentId = navigation?.extras.state['id']
      console.log(this.assessmentId);
      this.assessment.id = this.assessmentId;
    }

    const today = new Date();
    this.minDate = today.toISOString().split('T')[0]; // Data de hoje
    this.maxDate = new Date(today.getFullYear() + 5, today
      .getMonth(), today.getDate()).toISOString().split('T')[0]; // Até 5 anos no futuro

  }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();

    // BUSCAR ASSESSMENT E HEADER
    this.http.get<Assessment>(this.apiUrlAssessment + `${this.assessmentId}`)
      .pipe(
        map((response) => {
          // ATUALIZAR O ASSESSMENT COM A RESPOSTA
          this.assessment = response;
          console.log('Assessment carregado:', response);

          // RETORNAR O ID DO HEADER
          return this.assessment.headerId;
        }),
        // ENCADEIA A REQUISIÇÃO PARA BUSCAR O HEADER
        switchMap((headerId) => {
          return this.http.get<Header>(this.apiUrlHeader + `${headerId}`);
        })
      )
      .subscribe({
        next: (headerResponse) => {
          // ATUALIZAR O HEADER COM A RESPOSTA
          this.header = headerResponse;
          console.log('Header carregado:', headerResponse);
        },
        error: (error) => {
          console.error('Erro ao carregar avaliação ou cabeçalho:', error);
        }
      });

  }

  return_assessment() {
    this.router.navigateByUrl("/avaliacao");
  }

  validateQuantity() {
    if (this.appliesAssessment.quantity < 1) {
      this.appliesAssessment.quantity = 1;
    }
    if (this.appliesAssessment.quantity > 100) {
      this.appliesAssessment.quantity = 100;
    }
  }

  validateDate() {
    if (new Date(this.appliesAssessment.applicationDate) < new Date(this.minDate)) {
      alert('Data de aplicação não pode ser no passado!');
      this.appliesAssessment.applicationDate = this.minDate;
    }
  }

  save_applied_assessment() {
    this.http.post
  }

  headerQuestion: QuestionHeader = {
    id: 1,
    content: "Qual é a capital da França?",
    image_path: ""
  }

  alternativesQuestion: Alternative[] = [
    {
      id: 1,
      option: "A",
      description: "Paris",
      imagePath: "images/paris.jpg",
      isCorrect: true,
      explanation: "Paris é a capital da França, conhecida por sua história e cultura.",
      question_id: 1
    },
    {
      id: 2,
      option: "B",
      description: "Londres",
      imagePath: "images/londres.jpg",
      isCorrect: false,
      explanation: "Londres é a capital do Reino Unido, não da França.",
      question_id: 1
    },
    {
      id: 3,
      option: "C",
      description: "Berlim",
      imagePath: "images/berlim.jpg",
      isCorrect: false,
      explanation: "Berlim é a capital da Alemanha, não da França.",
      question_id: 1
    },
    {
      id: 4,
      option: "D",
      description: "Madrid",
      imagePath: "images/madrid.jpg",
      isCorrect: false,
      explanation: "Madrid é a capital da Espanha, não da França.",
      question_id: 1
    }
  ]

  questionExample: Question[] = [{
    id: 1,
    multipleChoice: true,
    numberLines: 3,
    personId: 101,
    header: this.headerQuestion,
    answerId: 2,
    difficultyLevel: 2,
    enable: true,
    educationLevel: 3,
    accessLevel: 1,
    tagIds: [1, 2],
    alternatives: this.alternativesQuestion
  }];
}
