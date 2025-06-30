import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';

type LoginForm = {
  email: "string",
  password: "string"
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
