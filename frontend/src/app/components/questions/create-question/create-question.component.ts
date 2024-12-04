import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { GerarQuestaoComponent } from '../../../modal/gerar-questao/gerar-questao.component';
import { UserService } from '../../../services/user.service';
import { QuestionService } from '../../../services/question-service/question-service.service';
import { Alternative } from '../../../types/dto/Alternative';
import { Question } from '../../../types/dto/Question';
import { QuestionHeader } from '../../../types/dto/QuestionHeader';
import { Observable } from 'rxjs';
import { UserData } from '../../../types/dto/UserData';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-create-question',
  standalone: true,
  imports: [CommonModule, FormsModule, GerarQuestaoComponent, ReactiveFormsModule, RouterModule],
  templateUrl: './create-question.component.html',
  styleUrl: './create-question.component.css'
})
export class CreateQuestionComponent implements OnInit{

  constructor(
    private userService: UserService,
    private questionService: QuestionService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

  user$!: Observable<UserData | null>;
  userId = 0

  questionId: number | null = null;

  question: Question | null = null;

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => this.userId = user == null ? 0 : user.id);
    this.route.paramMap.subscribe(params => {
      this.questionId = +params.get('id')!;
      if (this.questionId) {
        this.loadQuestion(this.questionId);
      }
    });
  }

  questionForm = new FormBuilder().group({
    enunciado: ["", [Validators.required]],
    alternativa: ["", [Validators.required]], explicacao: [""], isCorrectA: [],
    alternativaB: [""], explicacaoB: [""], isCorrectB: [],
    alternativaC: [""], explicacaoC: [""], isCorrectC: [],
    alternativaD: [""], explicacaoD: [""], isCorrectD: [],
    alternativaE: [""], explicacaoE: [""], isCorrectE: [],
  })

  niveis = [
    { label: 'ENSINO FUNDAMENTAL', value: 0 },
    { label: 'ENSINO MÉDIO', value: 1 },
    { label: 'ENSINO SUPERIOR', value: 2 }
  ];

  categorias = [
    { label: 'MATEMÁTICA', value: 0 },
    { label: 'PORTUGUÊS', value: 1 },
    { label: 'FÍSICA', value: 2 }
  ];

  selectedNivel: number | null = null;
  selectedCategoria: number | null = null;

  discursiva: boolean = false;
  numberLines: number = 0;
  access: boolean = false;

  modalEnabled = false;

  loadQuestion(id: number){
    this.questionService.getQuestionById(id).subscribe(response => {
      this.question = response; 
      console.log(response)
      this.selectedCategoria = response.tagIds![0];
      this.selectedNivel = response.educationLevel ==? "PRIVATE";
    
    });
  }

  generateQuestion() {
    this.mostrarModal();
  }

  cancel() {
    this.router.navigate(['/questions']);
  }

  onNivelChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedNivel = Number(selectElement.value);
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

  onSubmit() {

    const values = this.questionForm.value;
    
    const alternatives: Alternative[] = [
      { id: null, option: 'A', description: values.alternativa!, imagePath: '', isCorrect: values.isCorrectA! === 1, explanation: values.explicacao!, question_id: null },
      { id: null, option: 'B', description: values.alternativaB!, imagePath: '', isCorrect: values.isCorrectB! === 2, explanation: values.explicacaoB!, question_id: null },
      { id: null, option: 'C', description: values.alternativaC!, imagePath: '', isCorrect: values.isCorrectC! === 3, explanation: values.explicacaoC!, question_id: null },
      { id: null, option: 'D', description: values.alternativaD!, imagePath: '', isCorrect: values.isCorrectD! === 4, explanation: values.explicacaoD!, question_id: null },
      { id: null, option: 'E', description: values.alternativaE!, imagePath: '', isCorrect: values.isCorrectE! === 5, explanation: values.explicacaoE!, question_id: null }
    ];

    const header: QuestionHeader = {id:null, content:values.enunciado!, image_path:''}

    const question: Question = {
      id: this.questionId ?? 0,
      multipleChoice: !this.discursiva,
      numberLines: this.numberLines,
      personId: this.userId,
      header: header,
      answerId: 0,
      difficultyLevel: this.selectedNivel ?? 0,
      enable: true,
      educationLevel: this.selectedNivel ?? 0,
      accessLevel: this.selectedCategoria ?? 0,
      tagIds: [this.selectedCategoria ?? 0],
      alternatives: alternatives
    };

    this.questionService.createQuestion(question).subscribe(response => {
      console.log('Questão criada com sucesso:', response);
    }, error => {
      console.error('Erro ao criar a questão:', error);
    });
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }
}
