import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class OpenAiService {

  private readonly backendUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  generateText(prompt: string): Observable<any> {

  return this.http.post(`${this.backendUrl}/ai/openai`, { prompt }, {
    headers: {
      'Content-Type': 'application/json'
    },
    responseType: 'json'
  });
}


  chat(message: string): Observable<any> {
    return this.http.post(`${this.backendUrl}/ai/chat`, { message }, {
      headers: { 'Content-Type': 'application/json' }
    });
  }
}
