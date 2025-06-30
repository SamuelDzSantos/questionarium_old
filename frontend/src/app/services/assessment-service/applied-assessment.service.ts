import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../../environments/environment';
import { AppliedAssessment, ApplyAssessmentRequest } from '../../types/dto';

@Injectable({
    providedIn: 'root'
})
export class AppliedAssessmentService {
    private baseUrl = `${env.baseUrl}/applied-assessments`;

    constructor(private http: HttpClient) { }

    private getHeaders(userId: number, isAdmin: boolean): HttpHeaders {
        return new HttpHeaders({
            'X-User-id': userId.toString(),
            'X-User-isAdmin': isAdmin ? 'true' : 'false'
        });
    }

    /** 1. Criar aplicação de modelo */
    apply(request: ApplyAssessmentRequest, userId: number, isAdmin: boolean): Observable<AppliedAssessment> {
        return this.http.post<AppliedAssessment>(this.baseUrl, request, { headers: this.getHeaders(userId, isAdmin) });
    }

    /** 2. Buscar aplicação por id */
    getById(id: number, userId: number, isAdmin: boolean): Observable<AppliedAssessment> {
        return this.http.get<AppliedAssessment>(`${this.baseUrl}/${id}`, { headers: this.getHeaders(userId, isAdmin) });
    }

    /** 3. Listar todas as aplicações (admin vê tudo, user só ativas) */
    getAll(userId: number, isAdmin: boolean): Observable<AppliedAssessment[]> {
        return this.http.get<AppliedAssessment[]>(this.baseUrl, { headers: this.getHeaders(userId, isAdmin) });
    }

    /** 4. Listar aplicações do usuário logado */
    getByUser(): Observable<AppliedAssessment[]> {
        return this.http.get<AppliedAssessment[]>(`${this.baseUrl}/user`);
    }

    /** 5. Inativar (soft delete) uma aplicação */
    delete(id: number, userId: number, isAdmin: boolean): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`, { headers: this.getHeaders(userId, isAdmin) });
    }

    findWithFilter(
        description?: string
    ): Observable<AppliedAssessment[]> {
        let params = new HttpParams();
        if (description !== undefined) params = params.set('description', description);
        return this.http.get<AppliedAssessment[]>(`${this.baseUrl}/user`, { params });

    }

}
