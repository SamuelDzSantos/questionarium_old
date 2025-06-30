import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AssessmentHeader } from '../../types/dto/AssessmentHeader';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserData } from '../../types/dto/UserData';
import { Observable } from 'rxjs';
import { UserInfo } from '../../interfaces/user/user-info.data';

@Component({
  selector: 'app-consultar-cabecalho-modal',
  templateUrl: './consultar-cabecalho.component.html',
  styleUrl: './consultar-cabecalho.component.css',
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class ConsultarCabecalhoComponent implements OnInit {

  @Output() closeModalEvent = new EventEmitter<void>();
  @Output() clickedEvent = new EventEmitter<AssessmentHeader>();

  headers: AssessmentHeader[] = [];
  user$!: Observable<UserInfo | null>;
  userId = 0;
  isAdmin = false;

  // O endpoint correto do gateway:
  apiUrl = 'http://localhost:14000/assessment-headers';

  constructor(
    private http: HttpClient,
    private userService: UserService) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => {
      if (user) {
        this.userId = user.id;
        this.isAdmin = false; // Ajuste se seu sistema diferencia admin

        // Busca apenas os headers do usuário logado (endpoint padrão backend)
        const headers = new HttpHeaders({
          'X-User-id': this.userId.toString(),
          'X-User-isAdmin': this.isAdmin ? 'true' : 'false'
        });
        this.http.get<AssessmentHeader[]>(`${this.apiUrl}/user`, { headers })
          .subscribe({
            next: (response) => {
              this.headers = response;
            },
            error: (error) => {
              console.error('Erro ao carregar cabeçalhos:', error);
              alert('Erro ao carregar cabeçalhos.');
            }
          });
      }
    });
  }

  selectHeader(header: AssessmentHeader) {
    this.clickedEvent.emit(header);
    this.closeModalEvent.emit();
  }

  deleteHeader(header: AssessmentHeader) {
    let confirmDelete = confirm('Tem certeza que deseja excluir este cabeçalho?');
    if (confirmDelete) {
      const headers = new HttpHeaders({
        'X-User-id': this.userId.toString(),
        'X-User-isAdmin': this.isAdmin ? 'true' : 'false'
      });
      this.http.delete(`${this.apiUrl}/${header.id}`, { headers }).subscribe({
        next: () => {
          // Atualize a lista removendo o item excluído
          this.headers = this.headers.filter(h => h.id !== header.id);
          alert('Cabeçalho excluído com sucesso!');
        },
        error: (error) => {
          console.error('Erro ao excluir cabeçalho:', error);
          alert('Não foi possível excluir o cabeçalho. Tente novamente mais tarde.');
        },
      });
    }
  }

  closeModal() {
    this.closeModalEvent.emit();
  }
}
