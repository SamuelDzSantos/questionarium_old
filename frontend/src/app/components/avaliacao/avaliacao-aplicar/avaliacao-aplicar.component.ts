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
    if (!this.appliesAssessment.applicationDate) {
      alert('A data de aplicação é obrigatória!');
      return;
    }
  
    const appliedAssessmentPayload = {
      id: this.assessment.id,
      quantity: this.appliesAssessment.quantity,
      shuffle: this.appliesAssessment.shuffle,
      applicationDate: this.appliesAssessment.applicationDate,
    };
  
    this.http.post(`${this.apiUrlAppliedAssessment}applied-assessment`, appliedAssessmentPayload, { responseType: 'text' })
      .subscribe({
        next: (response) => {
          alert('Avaliação aplicada salva com sucesso!');
          this.return_assessment();
        },
        error: (error) => {
          console.error('Erro ao salvar avaliação aplicada:', error);
          alert('Erro ao salvar a aplicação da avaliação. Tente novamente.');
        }
      });
  }

  alternativesQuestion: Alternative[] = [
    {
      id: 1,
      // option: "A",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: true,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1,
      order: 1
    },
    {
      id: 2,
      // option: "B",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1,
      order: 2
    },
    {
      id: 3,
      // option: "C",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1,
      order: 3
    },
    {
      id: 4,
      // option: "D",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1,
      order: 3
    },
    {
      id: 5,
      // option: "E",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1,
      order: 4
    }
  ]

  questionExample: Question[] = [{
    id: 1,
    multipleChoice: true,
    numberLines: 3,
    personId: 101,
    header: "Enunciado",
    answerId: 2,
    difficultyLevel: 2,
    enable: true,
    educationLevel: 'ENSINO_FUNDAMENTAL',
    accessLevel: 1,
    tagIds: [1, 2],
    alternatives: this.alternativesQuestion
  }];
}
