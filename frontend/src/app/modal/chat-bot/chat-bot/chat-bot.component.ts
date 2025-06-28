import { Component, EventEmitter, Output } from '@angular/core';
import { OpenAIService } from '../../../services/openai-service/openai-service.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-chat-bot-modal',
  templateUrl: './chat-bot.component.html',
  styleUrls: ['./chat-bot.component.css'],
  imports: [FormsModule, CommonModule],
  standalone: true
})
export class ChatBotComponent {
  userInput: string = '';

  @Output() closeModalEvent = new EventEmitter<void>();

  constructor(private openAiService: OpenAIService) {}

  cancel() {
    this.closeModalEvent.emit()
  }

  closeModal() {
    this.closeModalEvent.emit()
  }

  messages: { sender: 'bot' | 'user'; text: string }[] = [
    { sender: 'bot', text: 'Olá! Em que posso ajudar?' },
  ];
  newMessage: string = '';

  sendMessage() {
    if (this.newMessage.trim()) {

      this.messages.push({ sender: 'user', text: this.newMessage });

      const userMessage = this.newMessage;

      this.newMessage = '';

    this.openAiService.chat(userMessage).subscribe(
      (response: any) => {
        this.messages.push({ sender: 'bot', text: response.response || 'Resposta não disponível no momento.' });
      },
      (error) => {
        this.messages.push({ sender: 'bot', text: 'Desculpe, houve um erro ao processar sua mensagem.' });
        console.error('Chat API error:', error);
      }
    );

    }
  }
}
