import { Injectable } from '@angular/core';
import { AppliedAssessmentReportTs } from '../shared/interfaces/applied-assessment-report.ts';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = environment.apiUrl;


  private methodUrls = {
    "getReportList": `${this.apiUrl}/report`,
  }

  public getAssessmentReportList() {
    return this.http.get<AppliedAssessmentReportTs[]>(this.methodUrls.getReportList);
  }


  constructor(private http: HttpClient) { }
}
