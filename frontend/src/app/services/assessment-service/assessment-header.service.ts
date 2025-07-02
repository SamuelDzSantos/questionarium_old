// import { Injectable } from '@angular/core';
// import { HttpClient, HttpHeaders } from '@angular/common/http';
// import { Observable } from 'rxjs';
// import { env } from '../../../environments/environment';
// import { AssessmentHeader } from '../../types/dto';

// @Injectable({
//   providedIn: 'root',
// })
// export class AssessmentHeaderService {
//   private baseUrl = `${env.baseUrl}/assessment-headers`;

//   constructor(private http: HttpClient) { }

//   private getHeaders(userId: number, isAdmin: boolean): HttpHeaders {
//     return new HttpHeaders({
//       'X-User-id': userId.toString(),
//       'X-User-isAdmin': isAdmin ? 'true' : 'false'
//     });
//   }

//   create(header: AssessmentHeader, userId: number, isAdmin: boolean): Observable<AssessmentHeader> {
//     return this.http.post<AssessmentHeader>(this.baseUrl, header, { headers: this.getHeaders(userId, isAdmin) });
//   }

//   getAll(userId: number, isAdmin: boolean): Observable<AssessmentHeader[]> {
//     return this.http.get<AssessmentHeader[]>(this.baseUrl, { headers: this.getHeaders(userId, isAdmin) });
//   }

//   getById(id: number, userId: number, isAdmin: boolean): Observable<AssessmentHeader> {
//     return this.http.get<AssessmentHeader>(`${this.baseUrl}/${id}`, { headers: this.getHeaders(userId, isAdmin) });
//   }

//   getByUser(userId: number, isAdmin: boolean): Observable<AssessmentHeader[]> {
//     return this.http.get<AssessmentHeader[]>(`${this.baseUrl}/user`, { headers: this.getHeaders(userId, isAdmin) });
//   }

//   update(id: number, header: AssessmentHeader, userId: number, isAdmin: boolean): Observable<AssessmentHeader> {
//     return this.http.put<AssessmentHeader>(`${this.baseUrl}/${id}`, header, { headers: this.getHeaders(userId, isAdmin) });
//   }

//   delete(id: number, userId: number, isAdmin: boolean): Observable<void> {
//     return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders(userId, isAdmin) });
//   }
// }
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../../environments/environment';
import { AssessmentHeader } from '../../types/dto';

@Injectable({
  providedIn: 'root',
})
export class AssessmentHeaderService {
  private baseUrl = `${env.baseUrl}/assessment-headers`;

  constructor(private http: HttpClient) { }

  create(header: AssessmentHeader): Observable<AssessmentHeader> {
    return this.http.post<AssessmentHeader>(this.baseUrl, header);
  }

  getAll(): Observable<AssessmentHeader[]> {
    return this.http.get<AssessmentHeader[]>(this.baseUrl);
  }

  getById(id: number): Observable<AssessmentHeader> {
    return this.http.get<AssessmentHeader>(`${this.baseUrl}/${id}`);
  }

  getByUser(): Observable<AssessmentHeader[]> {
    return this.http.get<AssessmentHeader[]>(`${this.baseUrl}/user`);
  }

  update(id: number, header: AssessmentHeader): Observable<AssessmentHeader> {
    return this.http.put<AssessmentHeader>(`${this.baseUrl}/${id}`, header);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
