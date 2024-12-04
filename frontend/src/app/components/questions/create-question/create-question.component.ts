import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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
    private cdr: ChangeDetectorRef
  ) {}

  user$!: Observable<UserData | null>;
  userId = 0

  questionId: number | null = null;

  question: Question | null = null;
  
  questionForm: any;

  niveis = [
    { label: 'ENSINO_FUNDAMENTAL', value: 0 },
    { label: 'ENSINO_MÉDIO', value: 1 },
    { label: 'ENSINO_SUPERIOR', value: 2 }
  ];

  categorias = [
    { label: 'MATEMÁTICA', value: 0 },
    { label: 'PORTUGUÊS', value: 1 },
    { label: 'FÍSICA', value: 2 }
  ];

  selectedNivel: number | null = null;
  selectedCategoria: number | null = null;
  difficultyLevel: number | null = null;

  discursiva: boolean = false;
  numberLines: number = 0;
  access: boolean = false;

  modalEnabled = false;

  ngOnInit(): void {
    this.questionForm = new FormBuilder().group({
      enunciado: ["", [Validators.required]],
      alternativa: ["", [Validators.required]], explicacao: [""], isCorrectA: [0],
      alternativaB: [""], explicacaoB: [""], isCorrectB: [0],
      alternativaC: [""], explicacaoC: [""], isCorrectC: [0],
      alternativaD: [""], explicacaoD: [""], isCorrectD: [0],
      alternativaE: [""], explicacaoE: [""], isCorrectE: [0],
    })
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user) => this.userId = user == null ? 0 : user.id);
    this.route.paramMap.subscribe(params => {
      this.questionId = +params.get('id')!;
      if (this.questionId) {
        this.loadQuestion(this.questionId);
      }
    });
  }

  loadQuestion(id: number): void {
    this.questionService.getQuestionById(id).subscribe((response) => {
      this.question = response;
      this.selectedCategoria = response.tagIds![0];
      this.selectedNivel = response.educationLevel;
      this.discursiva = !response.multipleChoice;
      this.access = response.accessLevel == 0 ? true : false;
      this.difficultyLevel = response.difficultyLevel;
  
      const alternativesMap = new Map<string, Alternative>();
      response.alternatives.forEach((alt) => {
        alternativesMap.set(alt.option, alt);
      });
  
      this.questionForm.patchValue({
        enunciado: response.header.content,
        alternativa: alternativesMap.get('A')?.description || '',
        explicacao: alternativesMap.get('A')?.explanation || '',
        isCorrectA: alternativesMap.get('A')?.isCorrect ? 1 : 0,
        alternativaB: alternativesMap.get('B')?.description || '',
        explicacaoB: alternativesMap.get('B')?.explanation || '',
        isCorrectB: alternativesMap.get('B')?.isCorrect ? 2 : 0,
        alternativaC: alternativesMap.get('C')?.description || '',
        explicacaoC: alternativesMap.get('C')?.explanation || '',
        isCorrectC: alternativesMap.get('C')?.isCorrect ? 3 : 0,
        alternativaD: alternativesMap.get('D')?.description || '',
        explicacaoD: alternativesMap.get('D')?.explanation || '',
        isCorrectD: alternativesMap.get('D')?.isCorrect ? 4 : 0,
        alternativaE: alternativesMap.get('E')?.description || '',
        explicacaoE: alternativesMap.get('E')?.explanation || '',
        isCorrectE: alternativesMap.get('E')?.isCorrect ? 5 : 0,
      });
    });
    this.cdr.detectChanges();
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
    console.log("onNivelChange")
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
      { id: this.question?.alternatives.find(a => a.option === 'A')?.id || null, option: 'A', description: values.alternativa!, imagePath: '', isCorrect: values.isCorrectA! === 1, explanation: values.explicacao!, question_id: this.question?.id || null },
      { id: this.question?.alternatives.find(a => a.option === 'B')?.id || null, option: 'B', description: values.alternativaB!, imagePath: '', isCorrect: values.isCorrectB! === 2, explanation: values.explicacaoB!, question_id: this.question?.id || null },
      { id: this.question?.alternatives.find(a => a.option === 'C')?.id || null, option: 'C', description: values.alternativaC!, imagePath: '', isCorrect: values.isCorrectC! === 3, explanation: values.explicacaoC!, question_id: this.question?.id || null },
      { id: this.question?.alternatives.find(a => a.option === 'D')?.id || null, option: 'D', description: values.alternativaD!, imagePath: '', isCorrect: values.isCorrectD! === 4, explanation: values.explicacaoD!, question_id: this.question?.id || null },
      { id: this.question?.alternatives.find(a => a.option === 'E')?.id || null, option: 'E', description: values.alternativaE!, imagePath: '', isCorrect: values.isCorrectE! === 5, explanation: values.explicacaoE!, question_id: this.question?.id || null }
    ];
  
    const header: QuestionHeader = {
      id: this.question?.header.id || null,
      content: values.enunciado!,
      image_path: this.question?.header.image_path || ''
    };
  
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
  
    if (this.questionId) {
      this.questionService.updateQuestion(this.questionId, question).subscribe(response => {
        console.log('Questão atualizada com sucesso:', response);
        this.router.navigate(['/questions']);
      });
    } else {
      this.questionService.createQuestion(question).subscribe(response => {
        console.log('Questão criada com sucesso:', response);
        this.router.navigate(['/questions']);
      });
    }
  }

  deleteQuestion(): void {
    const id = this.questionId!;
    this.questionService.deleteQuestion(id).subscribe(
      () => {
        console.log(`Questão com ID ${id} excluída com sucesso!`);
        this.router.navigate(['/questions']);
      },
      error => {
        console.error('Erro ao excluir a questão:', error);
      }
    );
  }
  

  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }
}
