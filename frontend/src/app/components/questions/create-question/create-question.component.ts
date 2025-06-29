import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { GerarQuestaoComponent } from '../../../modal/gerar-questao/gerar-questao.component';
import { UserService } from '../../../services/user.service';
import { Alternative } from '../../../types/dto/Alternative';
import { Question } from '../../../types/dto/Question';
import { Observable } from 'rxjs';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Tag } from '../../../types/dto/Tag';
import { QuestionService } from '../../../services/question-service/question-service.service';
import { UserInfo } from '../../../interfaces/user/user-info.data';

@Component({
  selector: 'app-create-question',
  standalone: true,
  imports: [CommonModule, FormsModule, GerarQuestaoComponent, ReactiveFormsModule, RouterModule],
  templateUrl: './create-question.component.html',
  styleUrl: './create-question.component.css'
})
export class CreateQuestionComponent implements OnInit {

  constructor(
    private userService: UserService,
    private questionService: QuestionService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) { }

  user$!: Observable<UserInfo | null>;
  userId = 0

  questionId: number | null = null;

  question: Question | null = null;

  questionForm: any;

  enunciado: string = '';
  header_image: string = '';

  niveis = [
    { label: 'ENSINO FUNDAMENTAL', value: 0, name: 'ENSINO_FUNDAMENTAL' },
    { label: 'ENSINO MÉDIO', value: 1, name: 'ENSINO_MÉDIO' },
    { label: 'ENSINO SUPERIOR', value: 2, name: 'ENSINO_SUPERIOR' }
  ];

  categorias: Tag[] = [];

  selectedNivel: number | undefined = undefined;
  selectedCategorias: number[] = [];

  discursiva: boolean = false;
  numberLines: number = 0;
  access: boolean = false;

  modalEnabled = false;

  showCorrectAlternativeError: boolean = false;
  showNoNivelSelected: boolean = false;

  ngOnInit(): void {
    this.questionForm = new FormBuilder().group({
      enunciado: ["", [Validators.required]],
      isCorrect: [null, [Validators.required]],
      alternativa: ["", [Validators.required]], explicacao: [""],// isCorrectA: [0],
      alternativaB: [""], explicacaoB: [""], // isCorrectB: [0],
      alternativaC: [""], explicacaoC: [""], // isCorrectC: [0],
      alternativaD: [""], explicacaoD: [""], // isCorrectD: [0],
      alternativaE: [""], explicacaoE: [""], // isCorrectE: [0],
    })
    this.questionService.getAllTags().subscribe(
      (tags: Tag[]) => {
        this.categorias = tags;
      }, (error) => {
        console.error('Error fetching tags:', error);
      }
    );
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
      this.selectedCategorias = response.tagIds!;
      this.selectedNivel = this.niveis.find(nivel => nivel.name === response.educationLevel)?.value;
      this.discursiva = !response.multipleChoice;
      this.access = response.accessLevel == 1 ? true : false;
      this.numberLines = response.numberLines ?? 0;

      console.log("*****************response********************")
      console.log(response)

      this.questionForm.patchValue({
        enunciado: response.header,
        alternativa: response.alternatives.find(alt => alt.alternativeOrder === 1)?.description || '',
        explicacao: response.alternatives.find(alt => alt.alternativeOrder === 1)?.explanation || '',
        isCorrectA: response.alternatives.find(alt => alt.alternativeOrder === 1)?.isCorrect ? 1 : 0,
        alternativaB: response.alternatives.find(alt => alt.alternativeOrder === 2)?.description || '',
        explicacaoB: response.alternatives.find(alt => alt.alternativeOrder === 2)?.explanation || '',
        isCorrectB: response.alternatives.find(alt => alt.alternativeOrder === 2)?.isCorrect ? 2 : 0,
        alternativaC: response.alternatives.find(alt => alt.alternativeOrder === 3)?.description || '',
        explicacaoC: response.alternatives.find(alt => alt.alternativeOrder === 3)?.explanation || '',
        isCorrectC: response.alternatives.find(alt => alt.alternativeOrder === 3)?.isCorrect ? 3 : 0,
        alternativaD: response.alternatives.find(alt => alt.alternativeOrder === 4)?.description || '',
        explicacaoD: response.alternatives.find(alt => alt.alternativeOrder === 4)?.explanation || '',
        isCorrectD: response.alternatives.find(alt => alt.alternativeOrder === 4)?.isCorrect ? 4 : 0,
        alternativaE: response.alternatives.find(alt => alt.alternativeOrder === 5)?.description || '',
        explicacaoE: response.alternatives.find(alt => alt.alternativeOrder === 5)?.explanation || '',
        isCorrectE: response.alternatives.find(alt => alt.alternativeOrder === 5)?.isCorrect ? 5 : 0,
      });

      const selectedCorrect = response.alternatives.find(alt => alt.isCorrect);

      if (selectedCorrect) {
        this.questionForm.patchValue({
          isCorrect: selectedCorrect.alternativeOrder,
        });
      }
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
  }

  onCategoriaChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;

    const selectedValues = Array.from(selectElement.selectedOptions)
      .map(option => {
        const value = option.value.trim();
        const match = value.match(/^(\d+)/);
        const numValue = match ? Number(match[1]) : NaN;
        return numValue;
      })
      .filter(value => !isNaN(value) && value !== -1);

    this.selectedCategorias = selectedValues;
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

    this.showCorrectAlternativeError = false;

    if (this.questionForm.get('isCorrect')?.value === null && this.discursiva === false) {
      this.showCorrectAlternativeError = true;
      return;
    }

    this.showNoNivelSelected = false;

    if (this.selectedNivel == undefined) {
      this.showNoNivelSelected = true;
      return;
    }

    const values = this.questionForm.value;

    const alternatives: Alternative[] = [
      { id: this.question?.alternatives[0]?.id || null, description: values.alternativa!, imagePath: '', isCorrect: values.isCorrect! == 1 ? true : this.discursiva? true : false, explanation: values.explicacao!, question_id: this.question?.id || null, alternativeOrder: 1 },
      { id: this.question?.alternatives[1]?.id || null, description: values.alternativaB!, imagePath: '', isCorrect: values.isCorrect! == 2, explanation: values.explicacaoB!, question_id: this.question?.id || null, alternativeOrder: 2 },
      { id: this.question?.alternatives[2]?.id || null, description: values.alternativaC!, imagePath: '', isCorrect: values.isCorrect! == 3, explanation: values.explicacaoC!, question_id: this.question?.id || null, alternativeOrder: 3 },
      { id: this.question?.alternatives[3]?.id || null, description: values.alternativaD!, imagePath: '', isCorrect: values.isCorrect! == 4, explanation: values.explicacaoD!, question_id: this.question?.id || null, alternativeOrder: 4 },
      { id: this.question?.alternatives[4]?.id || null, description: values.alternativaE!, imagePath: '', isCorrect: values.isCorrect! == 5, explanation: values.explicacaoE!, question_id: this.question?.id || null, alternativeOrder: 5 }
    ];

    const question: Question = {
      id: this.questionId ?? 0,
      multipleChoice: !this.discursiva,
      numberLines: this.numberLines,
      header: this.enunciado,
      header_image: this.header_image,
      answerId: 0,
      enable: true,
      // educationLevel: this.niveis.find(nivel => nivel.value === this.selectedNivel)?.name ?? 'ENSINO_FUNDAMENTAL',
      educationLevel: this.niveis.find(nivel => nivel.value === this.selectedNivel)?.name ?? '',
      accessLevel: this.access ? 1 : 0, //true == public, API 1 == publica
      tagIds: this.selectedCategorias,
      alternatives: this.discursiva ? [] : alternatives
    };

    if (this.questionId) {
      this.questionService.updateQuestion(this.questionId, question).subscribe(response => {
        console.log('Questão atualizada com sucesso:', response);
        this.router.navigate([`/questions`]);
      }, error => { console.log(error); alert("Erro no servidor. Tente novamente.") });
    } else {
      console.log(question)
      this.questionService.createQuestion(question).subscribe(response => {
        console.log('Questão criada com sucesso:', response);
        this.router.navigate([`/questions`]);
      }, error => { console.log(error); alert("Erro no servidor. Tente novamente.") });
    }
  }

  deleteQuestion(): void {
    if (!confirm("Deletar Questão?")) return;
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

  getTagName(tagId: number): string {
    const tag = this.categorias.find(c => c.id === tagId);
    return tag ? tag.name : '';
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }

  public carregarQuestaoGerada(question: Question) {
    console.log(question);

    this.enunciado = question.header;

    this.selectedNivel = this.niveis.find(nivel => nivel.name === question.educationLevel)?.value;

    this.selectedCategorias = question.tagIds!;

    this.discursiva = !question.multipleChoice;

    this.access = question.accessLevel === 1;

    this.questionForm.patchValue({
      enunciado: question.header,
      alternativa: question.alternatives.find(alt => alt.alternativeOrder === 1)?.description || '',
      explicacao: question.alternatives.find(alt => alt.alternativeOrder === 1)?.explanation || '',
      isCorrectA: question.alternatives.find(alt => alt.alternativeOrder === 1)?.isCorrect ? 1 : 0,
    })

    if (!this.discursiva) {
      this.questionForm.patchValue({
        alternativaB: question.alternatives.find(alt => alt.alternativeOrder === 2)?.description || '',
        explicacaoB: question.alternatives.find(alt => alt.alternativeOrder === 2)?.explanation || '',
        isCorrectB: question.alternatives.find(alt => alt.alternativeOrder === 2)?.isCorrect ? 2 : 0,
        alternativaC: question.alternatives.find(alt => alt.alternativeOrder === 3)?.description || '',
        explicacaoC: question.alternatives.find(alt => alt.alternativeOrder === 3)?.explanation || '',
        isCorrectC: question.alternatives.find(alt => alt.alternativeOrder === 3)?.isCorrect ? 3 : 0,
        alternativaD: question.alternatives.find(alt => alt.alternativeOrder === 4)?.description || '',
        explicacaoD: question.alternatives.find(alt => alt.alternativeOrder === 4)?.explanation || '',
        isCorrectD: question.alternatives.find(alt => alt.alternativeOrder === 4)?.isCorrect ? 4 : 0,
        alternativaE: question.alternatives.find(alt => alt.alternativeOrder === 5)?.description || '',
        explicacaoE: question.alternatives.find(alt => alt.alternativeOrder === 5)?.explanation || '',
        isCorrectE: question.alternatives.find(alt => alt.alternativeOrder === 5)?.isCorrect ? 5 : 0,
      })
    };

    const selectedCorrect = question.alternatives.find(alt => alt.isCorrect);
    if (selectedCorrect) {
      this.questionForm.patchValue({
        isCorrect: selectedCorrect.alternativeOrder,
      });
    }

    this.cdr.detectChanges();
  }

}
