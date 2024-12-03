import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AssessmentHeader } from '../types/dto/AssessmentHeader';

@Injectable({
  providedIn: 'root',
})
export class AssessmentHeaderService {
  private apiUrl = 'http://localhost:14000/header'; // URL do backend

  constructor(private http: HttpClient) {}

  createHeader(header: AssessmentHeader): Observable<AssessmentHeader> {
    return this.http.post<AssessmentHeader>(this.apiUrl, header);
  }

  getAllHeaders(): Observable<AssessmentHeader[]> {
    return this.http.get<AssessmentHeader[]>(this.apiUrl);
  }
}