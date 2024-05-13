import { Component } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { user_login } from '../../types/user_login';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  constructor(
    private formBuilder : FormBuilder,private router : Router){
  }

  loginForm = this.formBuilder.group({
    login: '',
    senha:  ''
  })

  onSubmit(){
    let nome = this.loginForm.value.login;
    if(nome != undefined && nome != null){
      let user_login : user_login = {logged:true,name:nome}
      localStorage.setItem("login",JSON.stringify(user_login))
      this.router.navigate([""]);  
    }
    }

}
