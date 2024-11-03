import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { HeaderComponent } from '../header/header.component';
import { ActivatedRoute } from '@angular/router';
import { RecuperarSenhaComponent } from "../recuperar-senha/recuperar-senha.component";
import { RecuperarSenhaModalComponent } from "../../modal/recuperar-senha-modal/recuperar-senha-modal.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, HeaderComponent, RecuperarSenhaComponent, RecuperarSenhaModalComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  loginForm = new FormBuilder().group({
    login: ["", [Validators.required, Validators.email]],
    senha: ["", [Validators.required]]
  })

  modalEnabled = false;

  constructor(private userService: UserService, private route: ActivatedRoute) { }

  ngOnInit() {
    //console.log(this.route.snapshot.data);
  }

  onSubmit() {
    let formValues = this.validateLoginForm();
    if (formValues)
      this.userService.login(formValues.login, formValues.senha);
  }

  private validateLoginForm() {
    let formValues = this.loginForm.value;
    let emailControl = this.loginForm.controls.login;

    if (formValues.login == undefined || formValues.login == null || formValues.login == "") {
      console.log(formValues.login)
      alert("Email é obrigatório!");
      return null;
    }
    if (!emailControl.valid) {
      alert("Formato de e-mail inválido")
      return null;
    } else if (formValues.senha == undefined || formValues.senha == null || formValues.senha == "") {
      alert("Senha é obrigatória!");
      return null;
    }
    return { login: formValues.login, senha: formValues.senha };
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }

}
