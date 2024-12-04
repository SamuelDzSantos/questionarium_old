import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable, switchMap } from 'rxjs';
import { UserService } from '../../../services/user.service';
import { UserData } from '../../../types/dto/UserData';

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

    // Primeiro, buscamos o Assessment.
    this.http.get<Assessment>(this.apiUrlAssessment + `${this.assessmentId}`)
      .pipe(
        map((response) => {
          // Atualizamos o Assessment com a resposta.
          this.assessment = response;
          console.log('Assessment carregado:', response);

          // Retornamos o ID do Header para a próxima requisição.
          return this.assessment.headerId;
        }),
        // Encadeamos a requisição para buscar o Header usando o ID retornado.
        switchMap((headerId) => {
          return this.http.get<Header>(this.apiUrlHeader + `${headerId}`);
        })
      )
      .subscribe({
        next: (headerResponse) => {
          // Atualizamos o Header com a resposta.
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
    // Você pode adicionar mais validações aqui, se necessário
    if (new Date(this.appliesAssessment.applicationDate) < new Date(this.minDate)) {
      alert('Data de aplicação não pode ser no passado!');
      this.appliesAssessment.applicationDate = this.minDate; // Ajusta a data para o mínimo permitido
    }
  }
}
