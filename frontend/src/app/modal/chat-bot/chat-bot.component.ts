import { Component, EventEmitter, Output } from '@angular/core';
import { OpenAiService } from '../../services/openai-service/open-ai.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-chat-bot',
  imports: [FormsModule, CommonModule],
  templateUrl: './chat-bot.component.html',
  styleUrl: './chat-bot.component.css'
})
export class ChatBotComponent {
  userInput: string = '';

  @Output() closeModalEvent = new EventEmitter<void>();

  constructor(private openAiService: OpenAiService) { }

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
        (error: any) => {
          this.messages.push({ sender: 'bot', text: 'Desculpe, houve um erro ao processar sua mensagem.' });
          console.error('Chat API error:', error);
        }
      );

    }
  }
}
