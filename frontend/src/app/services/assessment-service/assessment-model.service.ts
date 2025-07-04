// import { Injectable } from '@angular/core';
// import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
// import { Observable } from 'rxjs';
// import { env } from '../../../environments/environment';
// import { AssessmentModel, CreateAssessmentModelRequest } from '../../types/dto';

// @Injectable({
//     providedIn: 'root'
// })
// export class AssessmentModelService {
//     private baseUrl = `${env.baseUrl}/assessment-models`;

//     constructor(private http: HttpClient) { }

//     // Helper para headers do usuário logado
//     private getHeaders(userId: number, isAdmin: boolean): HttpHeaders {
//         return new HttpHeaders({
//             'X-User-id': userId.toString(),
//             'X-User-isAdmin': isAdmin ? 'true' : 'false'
//         });
//     }

//     getAll(userId: number, isAdmin: boolean): Observable<AssessmentModel[]> {
//         return this.http.get<AssessmentModel[]>(`http://localhost:14000/assessment-models/user`);
//     }

//     getById(id: number, userId: number, isAdmin: boolean): Observable<AssessmentModel> {
//         return this.http.get<AssessmentModel>(`${this.baseUrl} / ${id}`, { headers: this.getHeaders(userId, isAdmin) });
//     }

//     create(model: CreateAssessmentModelRequest, userId: number, isAdmin: boolean): Observable<AssessmentModel> {
//         return this.http.post<AssessmentModel>(this.baseUrl, model, { headers: this.getHeaders(userId, isAdmin) });
//     }

//     update(id: number, model: CreateAssessmentModelRequest, userId: number, isAdmin: boolean): Observable<AssessmentModel> {
//         return this.http.put<AssessmentModel>(`${this.baseUrl} / ${id}`, model, { headers: this.getHeaders(userId, isAdmin) });
//     }

//     delete(id: number): Observable<void> {
//         console.log(`${this.baseUrl}`)
//         return this.http.delete<void>(this.baseUrl + "/" + id);
//     }

//     getByUser(): Observable<AssessmentModel[]> {
//         return this.http.get<AssessmentModel[]>(`${this.baseUrl}/user`);
//     }

//     findWithFilter(
//         description?: string,
//         institution?: string,
//         classroom?: string,
//         course?: string,

//     ): Observable<AssessmentModel[]> {
//         let params = new HttpParams();
//         if (description !== undefined) params = params.set('description', description);
//         if (institution !== undefined) params = params.set('institution', institution);
//         if (classroom != null) params = params.set('classroom', classroom);
//         if (course != null) params = params.set('course', course);
//         return this.http.get<AssessmentModel[]>(`${this.baseUrl}/user`, { params });

//     }


// }

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../../environments/environment';
import { AssessmentModel, CreateAssessmentModelRequest } from '../../types/dto';
import { CustomModel } from '../../types/dto/CustomModel';

@Injectable({
    providedIn: 'root'
})
export class AssessmentModelService {
    private baseUrl = `${env.baseUrl}/assessment-models`;
    private baseUrl2 = `${env.baseUrl}/assessment/model`

    constructor(private http: HttpClient) { }

    getAll(): Observable<AssessmentModel[]> {
        return this.http.get<AssessmentModel[]>(`${this.baseUrl}`);
    }

    getById(id: number): Observable<AssessmentModel> {
        return this.http.get<AssessmentModel>(`${this.baseUrl}/${id}`);
    }

    create(model: CreateAssessmentModelRequest): Observable<AssessmentModel> {
        return this.http.post<AssessmentModel>(this.baseUrl, model);
    }

    update(id: number, model: CreateAssessmentModelRequest): Observable<AssessmentModel> {
        return this.http.put<AssessmentModel>(`${this.baseUrl}/${id}`, model);
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/${id}`);
    }

    getByUser(): Observable<AssessmentModel[]> {
        return this.http.get<AssessmentModel[]>(`${this.baseUrl}/user`);
    }

    getCreatedAssessment(id: number) {
        return this.http.get<CustomModel[]>(`${this.baseUrl2}/${id}`);
    }

    findWithFilter(
        description?: string,
        institution?: string,
        classroom?: string,
        course?: string
    ): Observable<AssessmentModel[]> {
        let params = new HttpParams();
        if (description !== undefined) params = params.set('description', description);
        if (institution !== undefined) params = params.set('institution', institution);
        if (classroom !== undefined) params = params.set('classroom', classroom);
        if (course !== undefined) params = params.set('course', course);
        return this.http.get<AssessmentModel[]>(`${this.baseUrl}/user`, { params });
    }
}
