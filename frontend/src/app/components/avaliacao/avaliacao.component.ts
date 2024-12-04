import { QuestionHeader } from './../../types/dto/QuestionHeader';
import { Question } from './../../types/dto/Question';
import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CriarCabecalhoComponent } from '../../modal/criar-cabecalho/criar-cabecalho.component';
import { Alternative } from '../../types/dto/Alternative';
import { UserService } from '../../services/user.service';
import { UserData } from '../../types/dto/UserData';
import { Observable } from 'rxjs';

export interface Assessment {
  id: number,
  questions: number[],
  creationDate: string,
  header: Header
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
  selector: 'app-avaliacao',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule, CriarCabecalhoComponent],
  templateUrl: './avaliacao.component.html',
  styleUrl: './avaliacao.component.css'
})
export class AvaliacaoComponent implements OnInit {
  user$!: Observable<UserData | null>;
  userId = 0;

  modalEnabled = false;

  @ViewChild('titulo') titulo!: ElementRef<HTMLElement>;

  montar() {
    this.titulo.nativeElement.textContent = "Selecione uma avaliação para montagem";
  }

  constructor(
    private router: Router,
    private userService: UserService) {

  }
  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
  }

  headers: Header[] = [{
    id: 1,
    department: "dep1",
    institution: "inst1",
    course: "Math",
    classroom: "A1",
    professor: "Razar",
    instructions: "Não cole",
    creationDate: "2024-08-10"
  }, {
    id: 2,
    department: "dep2",
    institution: "inst2",
    course: "Portugues",
    classroom: "B7",
    professor: "Razar",
    instructions: "Não cole",
    creationDate: "2024-01-10"
  }, {
    id: 3,
    department: "dep3",
    institution: "inst3",
    course: "Math",
    classroom: "A2",
    professor: "Luan",
    instructions: "Não cole",
    creationDate: "2024-09-10"
  }]

  assessments: Assessment[] = [{
    id: 1,
    questions: [1, 2, 3, 4, 5],
    creationDate: "2024-08-14",
    header: this.headers[0]
  }, {
    id: 2,
    questions: [1, 2, 3, 4, 5],
    creationDate: "2024-08-12",
    header: this.headers[1]
  }, {
    id: 3,
    questions: [1, 2, 3, 4, 5],
    creationDate: "2024-08-20",
    header: this.headers[2]
  }]

  select_assessment(id: number) {
    this.router.navigateByUrl("/avaliacao/aplicar", { state: { "id": id } })
  }

  questionsHeaders: QuestionHeader[] = [{
    id: 1,
    content: "Qual é a capital do Brasil?",
    image_path: ''
  }, {
    id: 2,
    content: "Quem escreveu 'Dom Casmurro'?",
    image_path: ''
  }, {
    id: 3,
    content: "Qual é o maior planeta do Sistema Solar?",
    image_path: ''
  }]

  alternatives: Alternative[] = [{
    id: 1,
    option: 'A',
    description: 'São Paulo',
    imagePath: '',
    isCorrect: false,
    explanation: 'Explicação da alternativa A',
    question_id: 1
  }, {
    id: 2,
    option: 'B',
    description: 'Brasília',
    imagePath: '',
    isCorrect: true,
    explanation: 'Explicação da alternativa B',
    question_id: 1
  }, {
    id: 3,
    option: 'C',
    description: 'Rio de Janeiro',
    imagePath: '',
    isCorrect: false,
    explanation: 'Explicação da alternativa C',
    question_id: 1
  }, {
    id: 4,
    option: 'A',
    description: 'José de Alencar',
    imagePath: '',
    isCorrect: false,
    explanation: 'Explicação da alternativa A',
    question_id: 2
  }, {
    id: 5,
    option: 'B',
    description: 'Machado de Assis',
    imagePath: '',
    isCorrect: true,
    explanation: 'Explicação da alternativa B',
    question_id: 2
  }, {
    id: 6,
    option: 'C',
    description: 'Monteiro Lobato',
    imagePath: '',
    isCorrect: false,
    explanation: 'Explicação da alternativa C',
    question_id: 2
  }, {
    id: 7,
    option: 'A',
    description: 'Júpiter',
    imagePath: '',
    isCorrect: true,
    explanation: 'Explicação da alternativa A',
    question_id: 3
  }, {
    id: 8,
    option: 'B',
    description: 'Saturno',
    imagePath: '',
    isCorrect: false,
    explanation: 'Explicação da alternativa B',
    question_id: 3
  }, {
    id: 9,
    option: 'C',
    description: 'Terra',
    imagePath: '',
    isCorrect: false,
    explanation: 'Explicação da alternativa C',
    question_id: 3
  }]




    questions: Question[] = [
    {
      id: 1,
      multipleChoice: true,
      numberLines: 3,
      personId: this.userId,
      header: this.questionsHeaders[0],
      answerId: 2,
      difficultyLevel: 1,
      enable: true,
      educationLevel: 2,
      accessLevel: 1,
      tagIds: [101, 102],
      alternatives: this.alternatives
    },
    {
      id: 2,
      multipleChoice: false,
      numberLines: 2,
      personId: 102,
      header: {
        id:null,
        content: "Quem escreveu 'Dom Casmurro'?",
        image_path: ""
      },
      answerId: 3,
      difficultyLevel: 2,
      enable: true,
      educationLevel: 3,
      accessLevel: 2,
      tagIds: [103, 104],
      alternatives: [
        { id: 1, option: "" ,description: "José de Alencar", isCorrect: false, imagePath: "", explanation: "explicação", question_id: null },
        { id: 2, option: "" ,description: "Machado de Assis", isCorrect: true, imagePath: "", explanation: "explicação", question_id: null },
        { id: 3, option: "" ,description: "Monteiro Lobato", isCorrect: false, imagePath: "", explanation: "explicação", question_id: null }
      ]
    },
    {
      id: 3,
      multipleChoice: true,
      numberLines: 1,
      personId: 103,
      header: {
        id: null,
        content: "Qual é o maior planeta do Sistema Solar?",
        image_path: ''
      },
      answerId: 1,
      difficultyLevel: 1,
      enable: true,
      educationLevel: 1,
      accessLevel: 1,
      tagIds: [105],
      alternatives: [
        { id: 1, option: "" ,description: "Júpiter", isCorrect: false, imagePath: "", explanation: "explicação", question_id: null },
        { id: 2, option: "" ,description: "Saturno", isCorrect: true, imagePath: "", explanation: "explicação", question_id: null },
        { id: 3, option: "" ,description: "Terra", isCorrect: false, imagePath: "", explanation: "explicação", question_id: null }
      ]
    }
  ];




}
