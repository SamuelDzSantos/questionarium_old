import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../../environments/environment';
import { AssessmentModel, CreateAssessmentModelRequest } from '../../types/dto';

@Injectable({
    providedIn: 'root'
})
export class AssessmentModelService {
    private baseUrl = `${env.baseUrl}/assessment-models`;

    constructor(private http: HttpClient) { }

    // Helper para headers do usuário logado
    private getHeaders(userId: number, isAdmin: boolean): HttpHeaders {
        return new HttpHeaders({
            'X-User-id': userId.toString(),
            'X-User-isAdmin': isAdmin ? 'true' : 'false'
        });
    }

    getAll(userId: number, isAdmin: boolean): Observable<AssessmentModel[]> {
        return this.http.get<AssessmentModel[]>(this.baseUrl, { headers: this.getHeaders(userId, isAdmin) });
    }

    getById(id: number, userId: number, isAdmin: boolean): Observable<AssessmentModel> {
        return this.http.get<AssessmentModel>(`${this.baseUrl}/${id}`, { headers: this.getHeaders(userId, isAdmin) });
    }

    create(model: CreateAssessmentModelRequest, userId: number, isAdmin: boolean): Observable<AssessmentModel> {
        return this.http.post<AssessmentModel>(this.baseUrl, model, { headers: this.getHeaders(userId, isAdmin) });
    }

    update(id: number, model: CreateAssessmentModelRequest, userId: number, isAdmin: boolean): Observable<AssessmentModel> {
        return this.http.put<AssessmentModel>(`${this.baseUrl}/${id}`, model, { headers: this.getHeaders(userId, isAdmin) });
    }

    delete(id: number, userId: number, isAdmin: boolean): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders(userId, isAdmin) });
    }

    getByUser(userId: number, isAdmin: boolean): Observable<AssessmentModel[]> {
        return this.http.get<AssessmentModel[]>(`${this.baseUrl}/user`, { headers: this.getHeaders(userId, isAdmin) });
    }
}
