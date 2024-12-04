import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { QuestionService } from '../../../services/question-service/question-service.service';
import { Question } from '../../../types/dto/Question';
import { UserService } from '../../../services/user.service';
import { Observable } from 'rxjs';
import { UserData } from '../../../types/dto/UserData';

@Component({
  selector: 'app-view-questions',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './view-questions.component.html',
  styleUrl: './view-questions.component.css'
})
export class ViewQuestionsComponent implements OnInit{
  user$!: Observable<UserData | null>;
  question$!: Observable<Question[]>;
  userId = 0
  questions : Question[] = [];

  constructor(private questionService: QuestionService, private userService: UserService){}  

  ngOnInit() {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => this.userId = user == null ? 0 : user.id);
    this.question$ = this.questionService.filterQuestions(undefined,this.userId);
    this.question$.subscribe((question) => this.questions = question == null ? [] : question);
  }
}
