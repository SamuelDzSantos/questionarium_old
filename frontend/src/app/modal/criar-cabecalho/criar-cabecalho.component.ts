import { HttpClient } from '@angular/common/http';
import { AssessmentHeader } from './../../types/dto/AssessmentHeader';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AvaliacaoCriarComponent } from '../../components/avaliacao/avaliacao-criar/avaliacao-criar.component';

@Component({
  selector: 'app-criar-cabecalho',
  standalone: true,
  imports: [FormsModule, AvaliacaoCriarComponent],
  templateUrl: './criar-cabecalho.component.html',
  styleUrl: './criar-cabecalho.component.css'
})
export class CriarCabecalhoComponent {

  header: AssessmentHeader = {

    id: 0,
    institution: '',
    department: '',
    course: '',
    classroom: '',
    professor: '',
    instructions: '',
    userId: 1
  };

  @Output() closeModalEvent = new EventEmitter<void>();

  apiUrl = 'http://localhost:14005/header';

  constructor(private http: HttpClient) { }

  saveHeader(): void {
    this.http.post<AssessmentHeader>(this.apiUrl, this.header).subscribe({
      next: (response) => {
        console.log('Cabeçalho criado:', response);
        alert('Cabeçalho criado com sucesso!');
        this.resetForm();
      },
      error: (error) => {
        console.error('Erro ao criar cabeçalho:', error);
      }
    });
  }

  resetForm(): void {
    this.header = {
      id: 0,
      institution: '',
      department: '',
      course: '',
      classroom: '',
      professor: '',
      instructions: '',
      userId: 1
    };
  }
}
