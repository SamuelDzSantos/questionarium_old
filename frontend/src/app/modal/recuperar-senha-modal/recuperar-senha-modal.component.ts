import { Component } from '@angular/core';
import { ResetPasswordValidation } from '../../types/dto/ResetPasswordValidation';
import { UserService } from '../../services/user.service';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-recuperar-senha-modal',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './recuperar-senha-modal.component.html',
  styleUrl: './recuperar-senha-modal.component.css'
})
export class RecuperarSenhaModalComponent {

  email_sent = false;
  remainingTime = 30;
  token = ""
  codigo = ""
  email = ""

  constructor(private userService: UserService, private router: Router) { }

  public enviar(email: string) {
    this.email = email;
    this.email_sent = true;
    this.userService.recuperarSenha(email);
    this.startTime();
  }

  public enviarCodigo(codigo: string) {
    let validation: ResetPasswordValidation = { "code": codigo, "email": this.email };
    this.userService.checkCodigo(validation).subscribe((token) => {
      this.token = token;
      localStorage.setItem("senha-token", token);
      this.router.navigateByUrl("/recuperar-senha")
    })
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
