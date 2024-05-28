import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { HeaderComponent } from '../header/header.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,HeaderComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  loginForm = new FormBuilder().group({
    login: ["", [Validators.required, Validators.email]],
    senha: ["", [Validators.required]]
  })

  constructor(private userService: UserService,private route:ActivatedRoute) { }

  ngOnInit(){
    console.log(this.route.snapshot.data);
  }

  onSubmit() {
    let formValues = this.loginForm.value;
    if (formValues.login != undefined && formValues.senha != undefined) {
      this.userService.login(formValues.login,formValues.senha);
    }
  }



}
