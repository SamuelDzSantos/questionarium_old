import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';


@Component({
  selector: 'app-recuperar-senha',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './recuperar-senha.component.html',
  styleUrl: './recuperar-senha.component.css'
})
export class RecuperarSenhaComponent {

  email_sent = false;
  remainingTime = 30;
  token = ""

  constructor(private userService: UserService) { }

  public enviar(email: string) {
    this.email_sent = true;
    this.userService.recuperarSenha(email).subscribe((token) => {
      this.token = token;
      console.log(token);
    })
    this.startTime();
  }

  private startTime() {
    let timer = setInterval(() => {
      this.remainingTime -= 1;
    }, 1000);

    setTimeout(() => {
      clearInterval(timer);
      this.remainingTime = 30;
    }, 1000 * 30);
  }
  reenviarCodigo() {
    if (this.remainingTime >= 30) {
      this.startTime();
    }
  }

}
