import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { QuestionService } from '../../../services/question-service/question-service.service';
import { Question } from '../../../types/dto/Question';
import { UserService } from '../../../services/user.service';
import { Observable } from 'rxjs';
import { UserData } from '../../../types/dto/UserData';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-view-questions',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './view-questions.component.html',
  styleUrl: './view-questions.component.css'
})
export class ViewQuestionsComponent implements OnInit{
  user$!: Observable<UserData | null>;
  question$!: Observable<Question[]>;
  userId = 0
  questions : Question[] = [];

  niveis = [
    { label: 'ENSINO_FUNDAMENTAL', value: 0 },
    { label: 'ENSINO_MÉDIO', value: 1 },
    { label: 'ENSINO_SUPERIOR', value: 2 }
  ];

  categorias = [
    { label: 'MATEMÁTICA', value: 0 },
    { label: 'PORTUGUÊS', value: 1 },
    { label: 'FÍSICA', value: 2 }
  ];

  selectedNivel: number | null = null;
  selectedCategoria: number | null = null;
  difficultyLevel: number | null = null;

  discursiva: boolean = false;
  numberLines: number = 0;
  access: boolean = false;

  constructor(private questionService: QuestionService, private userService: UserService, private router : Router){}  

  ngOnInit() {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => this.userId = user == null ? 0 : user.id);
    this.question$ = this.questionService.filterQuestions(undefined,this.userId);
    this.question$.subscribe((question) => this.questions = question == null ? [] : question);
  }

  viewQuestion(id:number){
    this.router.navigate(['/questions/', id])
  }

  onNivelChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedNivel = Number(selectElement.value);
    console.log("onNivelChange")
  }

  onCategoriaChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedNivel = Number(selectElement.value);
  }

  onDiscursivaChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.discursiva = checkbox.checked;
  }

  onAccessChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.access = checkbox.checked;
  }
}
