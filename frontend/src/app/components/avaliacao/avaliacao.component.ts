import { Alternative } from './../../types/dto/Alternative';
import { QuestionHeader } from './../../types/dto/QuestionHeader';
import { Question } from './../../types/dto/Question';
import { CommonModule } from '@angular/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { CriarCabecalhoComponent } from '../../modal/criar-cabecalho/criar-cabecalho.component';
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
  searchValue = ""
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

  select_assessment(id: number) {
    this.router.navigateByUrl("/avaliacao/aplicar", { state: { "id": id } })
  }

  search(){
    console.log(this.searchValue)
    if(this.searchValue != '' && this.searchValue != undefined && this.searchValue != null){
      
    }
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
    questions: [1],
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

  headerQuestion: QuestionHeader = {
    id: 1,
    content: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
    image_path: ""
  }

  alternativesQuestion: Alternative[] = [
    {
      id: 1,
      option: "A",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: true,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1
    },
    {
      id: 2,
      option: "B",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1
    },
    {
      id: 3,
      option: "C",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
      question_id: 1
    },
    {
      id: 4,
      option: "D",
      description: "Lorem Ipsum",
      imagePath: "",
      isCorrect: false,
      explanation: "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
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
