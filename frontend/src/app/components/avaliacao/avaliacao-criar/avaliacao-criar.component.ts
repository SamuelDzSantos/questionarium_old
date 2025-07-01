import { Component } from '@angular/core';
import { AssessmentHeader } from '../../../types/dto/AssessmentHeader';
import { AssessmentHeaderService } from '../../../services/assessment-service/assessment-header.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CriarCabecalhoComponent } from '../../../modal/criar-cabecalho/criar-cabecalho.component';
import { ConsultarCabecalhoComponent } from '../../../modal/consultar-cabecalho/consultar-cabecalho.component';
import { Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { UserData } from '../../../types/dto/UserData';
import { Observable } from 'rxjs';
import { UserInfo } from '../../../interfaces/user/user-info.data';
import { CreateAssessmentModelRequest } from '../../../types/dto';
import { QuestionDTO } from '../../../types/dto/QuestionDTO';
import { EscolherQuestao } from "../../../modal/escolher-questao/escolher-questao";



interface customQuestion extends QuestionDTO {
  weight: number
}

interface customModel {
  description: string,
  institution: string,
  department: string,
  course: string,
  classroom: string,
  professor: string,
  instructions: string,
  image: string,
  questions: customQuestion[]
}

@Component({
  selector: 'app-avaliacao-criar',
  standalone: true,
  imports: [FormsModule, CommonModule, CriarCabecalhoComponent, ConsultarCabecalhoComponent, EscolherQuestao],
  templateUrl: './avaliacao-criar.component.html',
  styleUrl: './avaliacao-criar.component.css'
})
export class AvaliacaoCriarComponent {
  modalCreateCabecalhoEnabled = false;
  modalGetCabecalhoEnabled = false;
  modalGetQuestionEnabled = false;


  user$!: Observable<UserInfo | null>;
  userId: number = 0;
  isAdmin: boolean = false;

  model: customModel = {
    description: '',
    institution: '',
    department: '',
    course: '',
    classroom: '',
    professor: '',
    instructions: '',
    image: '',
    questions: []
  }

  header: AssessmentHeader = {
    id: 0,
    institution: '',
    department: '',
    course: '',
    classroom: '',
    professor: '',
    instructions: '',
    userId: 0,
    creationDateTime: '',
    updateDateTime: ''
  };



  constructor(
    private headerService: AssessmentHeaderService,
    private router: Router,
    private userService: UserService
  ) {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe(user => {
      if (user) {
        this.userId = user.id;
        this.isAdmin = false;
        this.header.userId = user.id;
      }
    });
  }

  pad(num: string, size: number) {
    let s = num + "";
    while (s.length < size) s = "0" + s;
    return s;
  }

  createHeader() {
    this.mostrarModalCreate();
  }

  getHeaders() {
    this.mostrarModal();
  }

  mostrarModalCreate() {
    this.modalCreateCabecalhoEnabled = true;
  }

  mostrarModal() {
    this.modalGetCabecalhoEnabled = true;
  }

  setHeader(header: AssessmentHeader) {
    this.header = header;
  }

  saveHeader(): void {
    this.headerService.create(this.header, this.userId, this.isAdmin).subscribe({
      next: (response) => {
        console.log('Cabeçalho salvo:', response);
        this.header = response;
        alert('Cabeçalho salvo com sucesso!');
      },
      error: (err) => {
        console.error('Erro ao salvar cabeçalho:', err);
        alert('Erro ao salvar cabeçalho!');
      },
    });
  }

  loadHeader(): void {
    this.headerService.getByUser(this.userId, this.isAdmin).subscribe({
      next: (headers) => {
        if (headers.length > 0) {
          this.header = headers[0];
          alert('Cabeçalho carregado!');
        } else {
          alert('Nenhum cabeçalho encontrado.');
        }
      },
      error: (err) => {
        console.error('Erro ao carregar cabeçalho:', err);
        alert('Erro ao carregar cabeçalho!');
      }
    });
  }

  public fecharCreateCabecalhoModal() {
    this.modalCreateCabecalhoEnabled = false;
  }

  public fecharGetCabecalhoModal() {
    this.modalGetCabecalhoEnabled = false;
  }


  public mostrarModalQuestao() {
    this.modalGetQuestionEnabled = true;
  }

  public fecharModalQuestao() {
    this.modalGetQuestionEnabled = false;
  }

  return_assessment() {
    this.router.navigateByUrl("/avaliacao");
  }

  trackByIndex(index: number, item: customQuestion): number {
    return index;
  }

  public removerQuestao() {

  }



}
