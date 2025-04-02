import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { env } from '../../enviroments/enviroment';

@Injectable({
  providedIn: 'root',
})
export class OpenAIService {
  private apiUrl = 'https://api.openai.com/v1/chat/completions';
  private apiKey = env.openAiApiKey;
  private aiServiceUrl = 'http://localhost:5001/chat'

  constructor(private http: HttpClient) {}

  generateText(prompt: string): Observable<any> {
  
    const body = {
      model: 'gpt-3.5-turbo',
      messages: [{ role: 'user', content: prompt }],
      max_tokens: 1000,
    };
    
    return this.http.post(this.apiUrl, body, {
      headers: {
        Authorization: `Bearer ${this.apiKey}`,
        'Content-Type': 'application/json',
      },
    });
  }

  chat(message: string): Observable<any> {
    
    return this.http.post(this.aiServiceUrl, { message }, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }



}
