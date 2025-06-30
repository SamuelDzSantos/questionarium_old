import { Injectable } from '@angular/core';
import { AppliedAssessmentReportTs } from '../shared/interfaces/applied-assessment-report.ts';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { AppliedReport } from '../shared/interfaces/applied-report.js';
import { TagDTO } from '../shared/interfaces/tag-dto.js';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  private apiUrl = environment.apiUrl;


  private methodUrls = {
    "getReportList": `${this.apiUrl}/report`,
    "getReportData": `${this.apiUrl}/report/assessment`,
    "getTags": `${this.apiUrl}/questions/tags"`
  }

  public getAssessmentReportList() {
    return this.http.get<AppliedAssessmentReportTs[]>(this.methodUrls.getReportList);
  }

  public getReportData(id: number) {
    return this.http.get<AppliedReport>(`${this.methodUrls.getReportData}/${id}`);
  }

  public getTag(tagId: string) {
    console.log("Tag" + tagId)
    return this.http.get<TagDTO>(`${this.apiUrl}/questions/tags/${tagId}`);

  }


  constructor(private http: HttpClient) { }
}
