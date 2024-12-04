import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Question } from '../../types/dto/Question';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private readonly baseUrl = 'http://127.0.0.1:14004/questions';

  constructor(private http: HttpClient) { }

  createQuestion(question: Question): Observable<Question> {
    return this.http.post<Question>(this.baseUrl, question);
  }

  getAllQuestions(): Observable<Question[]> {
    return this.http.get<Question[]>(this.baseUrl);
  }

  getQuestionById(id: number): Observable<Question> {
    return this.http.get<Question>(`${this.baseUrl}/${id}`);
  }

  updateQuestion(id: number, question: Question): Observable<Question> {
    return this.http.put<Question>(`${this.baseUrl}/${id}`, question);
  }

  deleteQuestion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  filterQuestions(
    multipleChoice?: boolean,
    personId?: number,
    difficultyLevel?: number,
    educationLevel?: string,
    accessLevel?: string,
    tagIds?: number[]
  ): Observable<Question[]> {
    let params = new HttpParams();
    if (multipleChoice !== undefined) params = params.set('multipleChoice', multipleChoice);
    if (personId !== undefined) params = params.set('personId', personId);
    if (difficultyLevel !== undefined) params = params.set('difficultyLevel', difficultyLevel);
    if (educationLevel) params = params.set('educationLevel', educationLevel);
    if (accessLevel) params = params.set('accessLevel', accessLevel);
    if (tagIds && tagIds.length > 0) params = params.set('tagIds', tagIds.join(','));
    
    return this.http.get<Question[]>(`${this.baseUrl}/filter`, { params });
  }
}
