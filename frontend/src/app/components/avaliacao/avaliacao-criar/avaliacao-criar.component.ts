import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AssessmentHeader } from '../../../types/dto/AssessmentHeader';
import { AssessmentHeaderService } from '../../../services/assessment-service/assessment-header.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CriarCabecalhoComponent } from '../../../modal/criar-cabecalho/criar-cabecalho.component';
import { ConsultarCabecalhoComponent } from '../../../modal/consultar-cabecalho/consultar-cabecalho.component';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../services/user.service';
import { UserData } from '../../../types/dto/UserData';
import { catchError, Observable } from 'rxjs';
import { UserInfo } from '../../../interfaces/user/user-info.data';
import { CreateAssessmentModelRequest, Question, QuestionWeight } from '../../../types/dto';
import { QuestionDTO } from '../../../types/dto/QuestionDTO';
import { EscolherQuestao } from "../../../modal/escolher-questao/escolher-questao";
import { QuestionService } from '../../../services/question-service/question-service.service';
import { CdkDrag, CdkDragDrop, CdkDropList, CdkDropListGroup, moveItemInArray } from '@angular/cdk/drag-drop';
import { CreateAssessmentModelRequestDTO } from '../../../types/dto/CreateAssessmentModelRequestDTO';
import { AssessmentModelService } from '../../../services/assessment-service/assessment-model.service';
import { CustomModel } from '../../../types/dto/CustomModel';
import { CustomQuestion } from '../../../types/dto/CustomQuestion';





@Component({
  selector: 'app-avaliacao-criar',
  standalone: true,
  imports: [FormsModule, CommonModule, CriarCabecalhoComponent, ConsultarCabecalhoComponent, EscolherQuestao, CdkDropListGroup, CdkDropList, CdkDrag],
  templateUrl: './avaliacao-criar.component.html',
  styleUrl: './avaliacao-criar.component.css'
})
export class AvaliacaoCriarComponent implements OnInit {
  modalCreateCabecalhoEnabled = false;
  modalGetCabecalhoEnabled = false;
  modalGetQuestionEnabled = false;

  user$!: Observable<UserInfo | null>;
  userId: number = 0;
  isAdmin: boolean = false;
  modelId!: number;

  assessmentModel!: CreateAssessmentModelRequest;

  model: CustomModel = {
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
    private userService: UserService,
    private questionService: QuestionService,
    private assessmentService: AssessmentModelService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
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


  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.modelId = +params.get('id')!;
      if (this.modelId) {
        this.loadAssessment(this.modelId);
      }
    })
  }

  private loadAssessment(modelId: number) {
    this.assessmentService.getCreatedAssessment(modelId).subscribe((model) => {
      this.model = model[0];
      console.log("Após carga")
      console.log(this.model)
      this.cdr.detectChanges();
    })
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
    this.headerService.create(this.header).subscribe({
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
    this.headerService.getByUser().subscribe({
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

  public drop(event: CdkDragDrop<CustomQuestion[]>) {
    moveItemInArray(this.model.questions, event.previousIndex, event.currentIndex);
  }

  return_assessment() {
    this.router.navigateByUrl("/avaliacao");
  }



  saveAssessment() {

    let questions: QuestionWeight[] = this.model.questions.map((q) => {

      let qw: QuestionWeight = {
        "weight": q.weight,
        "questionId": q.id || 0
      };
      return qw
    });

    this.assessmentModel = {
      'description': this.model.description,
      'classroom': this.header.classroom,
      'image': '',
      'course': this.header.course,
      'institution': this.header.institution,
      'instructions': this.header.instructions,
      'professor': this.header.professor,
      'questions': questions,
      "department": this.header.department
    }

    this.assessmentService.create(this.assessmentModel).subscribe(() => {
      console.log("OPA")
      this.router.navigateByUrl("/avaliacao")
    }, () => {
      alert("Erro ao criar avaliação")
    })

  }

  updateAssessment() {

    console.log("OPA")

    let questions: QuestionWeight[] = this.model.questions.map((question) => {
      let questionWeight: QuestionWeight = { "questionId": question.id || 0, "weight": question.weight }
      return questionWeight;
    })

    let model: CreateAssessmentModelRequest = {
      "classroom": this.model.classroom,
      "course": this.model.course,
      "department": this.model.department,
      "description": this.model.description,
      "image": this.model.image,
      "institution": this.model.institution,
      "instructions": this.model.institution,
      "professor": this.model.institution,
      "questions": questions
    };
    this.assessmentService.update(this.modelId, model);
  }

  trackByIndex(index: number, item: CustomQuestion): number {
    return index;
  }

  public removerQuestao(id: number | null) {
    if (id != null) {
      if (this.questionExists(id)) {
        this.model.questions = this.model.questions.filter((q) => q.id !== id);
      }
    }
  }

  escolherQuestao(id: number | null) {
    this.fecharModalQuestao();
    if (id != null) {
      this.questionService.getQuestionById(id).subscribe((q) => {
        console.log(q)
        let qDto = q as any as CustomQuestion;
        qDto.weight = 0
        if (!this.questionExists(id))
          this.model.questions.push(qDto);
        else
          alert("Questões identicas não podem ser adicionadas!")
      });
    }
  }

  private questionExists(id: number): boolean {
    return this.model.questions.filter((q) => {
      return q.id == id
    }).length != 0;
  }


  protected getTotalWeight() {

    let totalWeight = 0;

    if (this.model.questions) {
      this.model.questions.forEach((q) => { totalWeight += q.weight });
    }
    return totalWeight;
  }


}

ChangeDetectorRef