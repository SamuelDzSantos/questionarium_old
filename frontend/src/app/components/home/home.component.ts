import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChatBotComponent } from '../../modal/chat-bot/chat-bot.component';

@Component({
  selector: 'app-home',
  imports: [RouterModule, ChatBotComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  modalEnabled: boolean = false;

  openModal() {
    this.modalEnabled = true;
  }

  closeModal() {
    this.modalEnabled = false;
  }
}
