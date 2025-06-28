import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Question } from '../../types/dto/Question';
import { Tag } from '../../types/dto/Tag';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private readonly baseUrl = 'http://127.0.0.1:14006/questions';

  constructor(private http: HttpClient) { }

  createQuestion(question: Question): Observable<Question> {
    return this.http.post<Question>(this.baseUrl, question);
  }

  getAllQuestions(): Observable<Question[]> {
    return this.http.get<Question[]>(this.baseUrl);
  }

  getAllTags(): Observable<Tag[]> {
    return this.http.get<Tag[]>(`${this.baseUrl}/tags`);
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
    educationLevel?: number,
    accessLevel?: number,
    tagIds?: number[],
    enunciado?: string,
  ): Observable<Question[]> {
    let params = new HttpParams();
    if (multipleChoice !== undefined) params = params.set('multipleChoice', multipleChoice);
    if (personId !== undefined) params = params.set('personId', personId);
    if (educationLevel !== undefined) params = params.set('educationLevel', educationLevel);
    if (accessLevel != null) params = params.set('accessLevel', accessLevel);
    if (enunciado != null) params = params.set('header', enunciado);
    if (tagIds && tagIds.length > 0) {
      const tagIdsStr = tagIds.join(',');
      params = params.set('tagIds', tagIdsStr);
    }
    console.log(params)

    return this.http.get<Question[]>(`${this.baseUrl}`, { params });
  }
}
