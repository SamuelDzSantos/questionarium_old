import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserService } from '../../services/user.service';
import { RedefinirSenha } from '../../modal/redefinir-senha/redefinir-senha';

type LoginForm = {
  email: "string",
  password: "string"
}

@Component({
  selector: 'app-login',
  imports: [RouterModule, ReactiveFormsModule, RedefinirSenha],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup;
  modalEnabled = false;

  constructor(private formBuilder: FormBuilder, private userService: UserService) {

  }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: [''],
      password: ['']
    })
  }

  submitLogin() {
    let values = this.loginForm.value as LoginForm;
    this.userService.login(values.email, values.password)
  }


  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }
}
