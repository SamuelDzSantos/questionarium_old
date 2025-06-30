import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';



import { env } from '../../../environments/environment';
import { CreateRecordAssessmentRequest, PatchRecordAssessmentRequest, RecordAssessment, RecordAssessmentPublic } from '../../types/dto';

@Injectable({
    providedIn: 'root'
})
export class RecordAssessmentService {
    private baseUrl = `${env.baseUrl}/record-assessments`;

    constructor(private http: HttpClient) { }

    private getHeaders(userId: number, isAdmin: boolean): HttpHeaders {
        return new HttpHeaders({
            'X-User-id': userId.toString(),
            'X-User-isAdmin': isAdmin ? 'true' : 'false'
        });
    }

    /** 1. Criar registros em lote */
    createBatch(
        request: CreateRecordAssessmentRequest,
        userId: number,
        isAdmin: boolean
    ): Observable<RecordAssessment[]> {
        return this.http.post<RecordAssessment[]>(
            this.baseUrl,
            request,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 2. Buscar registro por id */
    getById(id: number, userId: number, isAdmin: boolean): Observable<RecordAssessment> {
        return this.http.get<RecordAssessment>(
            `${this.baseUrl}/${id}`,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 3. Listar todos os registros */
    getAll(userId: number, isAdmin: boolean): Observable<RecordAssessment[]> {
        return this.http.get<RecordAssessment[]>(
            this.baseUrl,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 4. Listar registros do usuário logado */
    getByUser(userId: number, isAdmin: boolean): Observable<RecordAssessment[]> {
        return this.http.get<RecordAssessment[]>(
            `${this.baseUrl}/user`,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 5. Soft-delete de um registro pelo usuário */
    delete(id: number, userId: number, isAdmin: boolean): Observable<void> {
        return this.http.delete<void>(
            `${this.baseUrl}/${id}`,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 6. Soft-delete de um registro pelo ADMIN */
    adminDelete(id: number, userId: number, isAdmin: boolean): Observable<void> {
        return this.http.delete<void>(
            `${this.baseUrl}/admin/${id}`,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 7. Consulta pública de um registro */
    getPublic(id: number, userId: number, isAdmin: boolean): Observable<RecordAssessmentPublic> {
        return this.http.get<RecordAssessmentPublic>(
            `${this.baseUrl}/public/${id}`,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }

    /** 8. PATCH: Atualiza studentAnswerKey e recalcula obtainedScore */
    patchStudentKey(
        id: number,
        patch: PatchRecordAssessmentRequest,
        userId: number,
        isAdmin: boolean
    ): Observable<RecordAssessment> {
        return this.http.patch<RecordAssessment>(
            `${this.baseUrl}/${id}`,
            patch,
            { headers: this.getHeaders(userId, isAdmin) }
        );
    }
}
