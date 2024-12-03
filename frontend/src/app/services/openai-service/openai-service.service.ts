import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class OpenAIService {
  private apiUrl = 'https://api.openai.com/v1/completions';
  private apiKey = 'SUA_API_KEY_AQUI';

  constructor(private http: HttpClient) {}

  generateText(prompt: string): Observable<any> {
    const body = {
      model: 'text-davinci-003',
      prompt: prompt,
      max_tokens: 150,
    };

    return this.http.post(this.apiUrl, body, {
      headers: {
        Authorization: `Bearer ${this.apiKey}`,
        'Content-Type': 'application/json',
      },
    });
  }
}
