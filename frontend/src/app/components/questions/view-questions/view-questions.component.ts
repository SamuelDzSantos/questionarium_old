import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NavigationStart, Router, RouterModule } from '@angular/router';
import { Observable } from 'rxjs';
import { UserInfo } from '../../../interfaces/user/user-info.data';
import { QuestionService } from '../../../services/question-service/question-service.service';
import { UserService } from '../../../services/user.service';
import { Question } from '../../../types/dto/Question';
import { Tag } from '../../../types/dto/Tag';
import { LocalStorageService } from '../../../services/local-storage.service';

@Component({
  selector: 'app-view-questions',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './view-questions.component.html',
  styleUrl: './view-questions.component.css'
})
export class ViewQuestionsComponent implements OnInit, AfterViewInit {
  question$!: Observable<Question[]>;
  questions: Question[] = [];

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


  @ViewChild('publicCheckbox') publicCheckbox!: ElementRef<HTMLInputElement>;
  @ViewChild('privateCheckbox') privateCheckbox!: ElementRef<HTMLInputElement>;

  @ViewChild('discursivaCheckbox') discursivaCheckbox!: ElementRef<HTMLInputElement>;
  @ViewChild('escolhaCheckbox') escolhaCheckbox!: ElementRef<HTMLInputElement>;


  constructor(private questionService: QuestionService, private userService: UserService, private router: Router, private localStorageService: LocalStorageService) {

  }


  ngAfterViewInit(): void {
    /*
       
    */
  }

  ngOnInit() {

    if (this.localStorageService.getToken("isMultipleChoice") != null) {
      this.discursiva = !this.localStorageService.getToken("isMultipleChoice")!;
      this.escolha = this.localStorageService.getToken("isMultipleChoice");
    }


    this.question$ = this.questionService.getAllQuestions();
    this.question$.subscribe((question) => this.questions = question == null ? [] : question);
    this.questionService.getAllTags().subscribe(
      (tags: Tag[]) => {
        this.categorias = tags;
      }, (error) => {
        console.error('Error fetching tags:', error);
      }
    );
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

    //TODO current userId
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
        this.questions = questions || [];
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

  viewQuestion(id: number) {
    this.router.navigate(['/questions/', id])
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

  onDiscursivaChange(changed: 'DISCURSIVA' | 'ESCOLHA'): void {



    if (changed === 'DISCURSIVA' && this.discursivaCheckbox.nativeElement.checked) {
      this.escolhaCheckbox.nativeElement.checked = false;
      this.escolha = false;
    } else if (changed === 'ESCOLHA' && this.escolhaCheckbox.nativeElement.checked) {
      this.discursivaCheckbox.nativeElement.checked = false;
      this.discursiva = false;
    }

  }

  onAccessChange(changed: 'public' | 'private'): void {
    if (changed === 'public' && this.publicCheckbox.nativeElement.checked) {
      this.privateCheckbox.nativeElement.checked = false;
      this.accessPrivate = false;
    } else if (changed === 'private' && this.privateCheckbox.nativeElement.checked) {
      this.publicCheckbox.nativeElement.checked = false;
      this.accessPublic = false;
    }
  }

  onFilterClick() {
    this.loadQuestions();
  }

  getTagName(tagId: number): string {
    const tag = this.categorias.find(c => c.id === tagId);
    return tag ? tag.name : '';
  }
}
