import { LocalStorageService } from './../../services/localStorageService';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AvaliacaoCriarComponent } from '../../components/avaliacao/avaliacao-criar/avaliacao-criar.component';
import { AssessmentHeader } from '../../types/dto/AssessmentHeader';
import { HttpClient } from '@angular/common/http';
import { UserData } from '../../types/dto/UserData';
import { Observable } from 'rxjs';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-criar-cabecalho-modal',
  templateUrl: './criar-cabecalho.component.html',
  styleUrl: './criar-cabecalho.component.css',
  standalone: true,
  imports: [FormsModule],
})

export class CriarCabecalhoComponent {

  header: AssessmentHeader = {
    id: 0,
    institution: 'institution',
    department: 'department',
    course: 'course',
    classroom: 'classroom',
    professor: 'professor',
    instructions: 'instructions',
    userId: 0,
  };


  user$!: Observable<UserData | null>;
  userId = 0

  constructor(
    private localStorageService: LocalStorageService,
    private http: HttpClient,
    private userService: UserService) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => this.userId = user == null ? 0 : user.id);
  }



  @Output() closeModalEvent = new EventEmitter<void>();

  apiUrl = 'http://localhost:14005/header';

  saveHeader(): void {
    console.log("Teste")
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
      userId: 0
    };
  }

  cancel() {
    this.closeModalEvent.emit()
  }

  closeModal() {
    this.closeModalEvent.emit()
  }
}
