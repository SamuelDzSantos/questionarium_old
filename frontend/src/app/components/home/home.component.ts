import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ChatBotComponent } from '../../modal/chat-bot/chat-bot/chat-bot.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterModule, ChatBotComponent, CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {

  modalEnabled: boolean = false;

  openModal(){
    this.modalEnabled= true;
  }

  closeModal(){
    this.modalEnabled = false;
  }

}
