import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { UserService } from '../../services/user.service';

type RegisterForm = {
  name: "string"
  email: "string",
  password: "string"
}

@Component({
  selector: 'app-cadastrar',
  imports: [RouterModule, ReactiveFormsModule],
  templateUrl: './cadastrar.component.html',
  styleUrl: './cadastrar.component.css'
})
export class CadastrarComponent implements OnInit {

  registerForm!: FormGroup;

  constructor(private formBuilder: FormBuilder, private userService: UserService) { }

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name: [''],
      email: [''],
      password: ['']
    })
  }

  submitLogin() {
    let values = this.registerForm.value as RegisterForm;
    this.userService.register(values.name, values.email, values.password)
  }
}
