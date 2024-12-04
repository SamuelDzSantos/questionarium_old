import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AssessmentHeader } from '../../types/dto/AssessmentHeader';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { UserService } from '../../services/user.service';
import { UserData } from '../../types/dto/UserData';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-consultar-cabecalho-modal',
  templateUrl: './consultar-cabecalho.component.html',
  styleUrl: './consultar-cabecalho.component.css',
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class ConsultarCabecalhoComponent implements OnInit {


  @Output() closeModalEvent = new EventEmitter<void>();

  headers: AssessmentHeader[] = [];

  @Output() clickedEvent = new EventEmitter<AssessmentHeader>();

  apiUrl = 'http://localhost:14005/header/';

  user$!: Observable<UserData | null>;
  userId = 0;

  click(index: number) {
    console.log(this.headers[index])
    this.clickedEvent.emit(this.headers[index]);
  }

  constructor(
    private http: HttpClient,
    private userService: UserService) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => {
      this.userId = user == null ? 0 : user.id;
      this.http.get<AssessmentHeader[]>(this.apiUrl + "user/" + `${this.userId}`)
        .subscribe({
          next: (response) => {
            this.headers = response; console.log(response);
          },
          error: (error) => {
            console.error('Erro ao carregar cabeçalhos:', error);
          },
        });
    }
    );
  }

  selectHeader(header: AssessmentHeader) {
    this.clickedEvent.emit(header);
    this.closeModalEvent.emit();
  }

  deleteHeader(header: AssessmentHeader) {
    let confirmDelete = confirm('Tem certeza que deseja excluir este cabeçalho?');
    if (confirmDelete) {
      this.http.delete(`${this.apiUrl}` + `${header.id}`).subscribe({
        next: () => {
          console.log('Cabeçalho excluído com sucesso.');
          // Atualize a lista removendo o item excluído
          this.headers = this.headers.filter(h => h.id !== header.id);
        },
        error: (error) => {
          console.error('Erro ao excluir cabeçalho:', error);
          alert('Não foi possível excluir o cabeçalho. Tente novamente mais tarde.');
        },
      });
    }
  }

  cancel() {
    this.closeModalEvent.emit()
  }

  closeModal() {
    this.closeModalEvent.emit()
  }
}
