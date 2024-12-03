import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-view-questions',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './view-questions.component.html',
  styleUrl: './view-questions.component.css'
})
export class ViewQuestionsComponent {
  questions = [
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
    {
      description: 'Enunciado Lorem Ipsum is simply dummy text of the printing and typesetting industry.',
    },
  ];
}
