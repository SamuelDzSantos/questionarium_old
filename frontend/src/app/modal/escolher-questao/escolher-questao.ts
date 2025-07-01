import { Component, EventEmitter, Output } from '@angular/core';
import { QuestionService } from '../../services/question-service/question-service.service';
import { Observable } from 'rxjs';
import { Question, Tag } from '../../types/dto';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { QuestionDTO } from '../../types/dto/QuestionDTO';

@Component({
  selector: 'app-escolher-questao-modal',
  imports: [FormsModule, CommonModule],
  templateUrl: './escolher-questao.html',
  styleUrl: './escolher-questao.css'
})
export class EscolherQuestao {

  question$!: Observable<Question[]>;
  questions: QuestionDTO[] = [];

  niveis = [
    { label: 'Ensino Fundamental', value: 0 },
    { label: 'Ensino Médio', value: 1 },
    { label: 'Ensino Superior', value: 2 }
  ];

  categorias: Tag[] = [];

  selectedCategorias: number[] = [];
  selectedNivel: number | null = null;

  discursiva: boolean = false;
  escolha: boolean = false;
  accessPublic: boolean = false;
  accessPrivate: boolean = false;

  enunciado: string | undefined = undefined;

  @Output() closeModalEvent = new EventEmitter<void>();




  constructor(private questionService: QuestionService) {

  }

  ngOnInit() {

    this.question$ = this.questionService.getAllQuestions();
    this.question$.subscribe((question) => this.questions = question == null ? [] : question as unknown as QuestionDTO[]);
    /*this.questionService.getAllTags().subscribe(
      (tags: Tag[]) => {
        this.categorias = tags;
      }, (error) => {
        console.error('Error fetching tags:', error);
      }
    );*/
  }

  getAcessLevel(level: number) {
    let lev: any = level;
    if (lev == "PRIVATE") {
      return "Privado";
    } else {
      return "Público"
    }
  }

  loadQuestions() {

    const labelNivel = this.niveis.find(nivel => nivel.value === this.selectedNivel)
    console.log("LEvel")

    this.question$ = this.questionService.filterQuestions(
      !this.discursiva,
      this.handleIsMultipleChoice(),
      labelNivel?.value != -1 ? labelNivel?.value : undefined,
      this.handleAcess(),
      this.selectedCategorias,
      this.enunciado
    );

    this.question$.subscribe({
      next: (questions) => {
        this.questions = questions as unknown as QuestionDTO[];
        console.log(questions)
      },
      error: (err) => {
        console.error('Error fetching questions', err);
        this.questions = [];
      }
    });
  }

  private handleAcess() {
    if (this.accessPublic == false && this.accessPrivate == false) {
      return undefined;
    }
    if (this.accessPublic) {
      return 1;
    }
    else {
      return 0;
    }
  }

  private handleIsMultipleChoice() {
    if (this.discursiva == false && this.escolha == false) {
      return undefined;
    }
    if (this.discursiva) {
      return 0;
    }
    return 1;
  }


  onNivelChange(event: Event): void {
    const selectElement = event.target as HTMLSelectElement;
    this.selectedNivel = Number(selectElement.value)
  }

  onCategoriaChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;

    const selectedValues = Array.from(selectElement.selectedOptions)
      .map(option => {
        const value = option.value.trim();
        const match = value.match(/^(\d+)/);
        const numValue = match ? Number(match[1]) : NaN;
        return numValue;
      })
      .filter(value => !isNaN(value) && value !== 0);

    if (selectedValues.length === 1 && selectedValues[0] === 0) {
      this.selectedCategorias = [];
    } else {
      this.selectedCategorias = selectedValues;
    }
  }


  onFilterClick() {
    this.loadQuestions();
  }

  getTagName(tagId: number): string {
    const tag = this.categorias.find(c => c.id === tagId);
    return tag ? tag.name : '';
  }

  closeModal() {
    this.closeModalEvent.emit();
  }

}
