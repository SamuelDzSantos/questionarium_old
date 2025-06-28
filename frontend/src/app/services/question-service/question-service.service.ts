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
    tagIds?: number[],
    accessLevel?: number,
    educationLevel?: number,
    header?: string
  ): Observable<Question[]> {
    let params = new HttpParams();

    if (multipleChoice != null) {
      params = params.set('multipleChoice', String(multipleChoice));
    }
    if (tagIds?.length) {
      params = params.set('tagIds', tagIds.join(','));
    }
    if (accessLevel != null) {
      params = params.set('accessLevel', String(accessLevel));
    }
    if (educationLevel != null) {
      params = params.set('educationLevel', String(educationLevel));
    }
    if (header) {
      params = params.set('header', header);
    }

    return this.http.get<Question[]>(`${this.baseUrl}`, { params });
  }

}
