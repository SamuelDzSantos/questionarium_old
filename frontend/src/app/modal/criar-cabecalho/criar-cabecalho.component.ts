import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AssessmentHeader } from '../../types/dto/AssessmentHeader';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserData } from '../../types/dto/UserData';
import { Observable } from 'rxjs';
import { UserService } from '../../services/user.service';
import { UserInfo } from '../../interfaces/user/user-info.data';

@Component({
  selector: 'app-criar-cabecalho-modal',
  templateUrl: './criar-cabecalho.component.html',
  styleUrl: './criar-cabecalho.component.css',
  standalone: true,
  imports: [FormsModule],
})
export class CriarCabecalhoComponent implements OnInit {

  header: AssessmentHeader = {
    id: 0,
    institution: '',
    department: '',
    course: '',
    classroom: '',
    professor: '',
    instructions: '',
    userId: 0,
  };

  user$!: Observable<UserInfo | null>;
  userId = 0;
  isAdmin = false;

  @Output() closeModalEvent = new EventEmitter<void>();

  apiUrl = 'http://localhost:14000/assessment-headers';

  constructor(
    private http: HttpClient,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => {
      if (user) {
        this.userId = user.id;
        this.isAdmin = false;
        this.header.userId = user.id;
      }
    });
  }

  saveHeader(): void {
    this.header.userId = this.userId;

    const headers = new HttpHeaders({
      'X-User-id': this.userId.toString(),
      'X-User-isAdmin': this.isAdmin ? 'true' : 'false'
    });

    this.http.post<AssessmentHeader>(this.apiUrl, this.header, { headers }).subscribe({
      next: (response) => {
        console.log('Cabeçalho criado:', response);
        alert('Cabeçalho criado com sucesso!');
        this.resetForm();
        this.closeModalEvent.emit();
      },
      error: (error) => {
        console.error('Erro ao criar cabeçalho:', error);
        alert('Erro ao criar cabeçalho!');
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
      userId: this.userId
    };
  }

  closeModal() {
    this.closeModalEvent.emit();
  }
}
