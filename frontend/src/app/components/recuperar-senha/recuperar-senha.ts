import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PasswordUpdateForm } from '../../types/user/password-update-form';
import { Router } from '@angular/router';

type LoginForm = {
  newPassword: "string",
  confirmPassword: "string"
}


@Component({
  selector: 'app-recuperar-senha',
  imports: [ReactiveFormsModule],
  templateUrl: './recuperar-senha.html',
  styleUrl: './recuperar-senha.css'
})
export class RecuperarSenha {

  loginForm!: FormGroup;
  modalEnabled = false;
  token: string = '';
  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {

  }

  ngOnInit(): void {


    let a = new URLSearchParams(window.location.search).get('token');
    this.token = a?.toString() || '';
    console.log(this.token)
    this.loginForm = this.formBuilder.group({
      newPassword: [''],
      confirmPassword: ['']
    })
  }

  submitLogin() {
    let values = this.loginForm.value as LoginForm;

    let form: PasswordUpdateForm = {
      "currentPassword": "",
      "newPassword": values.newPassword,
      "confirmPassword": values.confirmPassword
    }

    this.userService.tokenUpdatePassword(form, this.token).subscribe(() => {
      this.router.navigateByUrl("/login")
    });
    console.log(values);
    console.log(this.token)
  }


  public mostrarModal() {
    this.modalEnabled = true;
  }

  public fecharModal() {
    this.modalEnabled = false;
  }
}
