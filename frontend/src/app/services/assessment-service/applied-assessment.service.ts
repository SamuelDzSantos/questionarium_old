import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../../environments/environment';
import { AppliedAssessment, ApplyAssessmentRequest } from '../../types/dto';

@Injectable({
    providedIn: 'root'
})
export class AppliedAssessmentService {
    private baseUrl = `${env.baseUrl}/applied-assessments`;

    constructor(private http: HttpClient) { }

    apply(request: ApplyAssessmentRequest): Observable<AppliedAssessment> {
        return this.http.post<AppliedAssessment>(this.baseUrl, request);
    }

    getById(id: number): Observable<AppliedAssessment> {
        return this.http.get<AppliedAssessment>(`${this.baseUrl}/${id}`);
    }

    getAll(): Observable<AppliedAssessment[]> {
        return this.http.get<AppliedAssessment[]>(this.baseUrl);
    }

    getByUser(): Observable<AppliedAssessment[]> {
        return this.http.get<AppliedAssessment[]>(`${this.baseUrl}/user`);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    findWithFilter(description?: string, date?: string): Observable<AppliedAssessment[]> {
        let params = new HttpParams();
        if (description !== undefined && description.trim() !== '') {
            params = params.set('description', description);
        }
        if (date !== undefined && date.trim() !== '') {
            params = params.set('applicationDate', date);
        }
        return this.http.get<AppliedAssessment[]>(`${this.baseUrl}/user/filter`, { params });
    }
}
