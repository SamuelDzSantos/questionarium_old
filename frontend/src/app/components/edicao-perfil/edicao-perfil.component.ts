import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../../services/user.service';
import { LoggedUser } from '../../types/dto/LoggedUser';

@Component({
  selector: 'app-edicao-perfil',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './edicao-perfil.component.html',
  styleUrl: './edicao-perfil.component.css'
})
export class EdicaoPerfilComponent implements OnInit {

  user$!: Observable<LoggedUser | null> 
  originalEmail! : string;

  constructor(private userService:UserService){}
  
  ngOnInit(): void {
       this.userService.getCurrentUser().subscribe((user)=>{
          this.cadastroForm.patchValue({nome:user?.username,email:user?.email,senha:"",confirmaSenha:""})
          this.originalEmail = user?.email ? user?.email : ""
        })
    }

  cadastroForm = new FormBuilder().group({
    nome: ["",[Validators.required]],
    email: ["",[Validators.required,Validators.email]],
    senha: ["",[Validators.required]],
    confirmaSenha:["",Validators.required]
  })


  onSubmit(){
    let values = this.cadastroForm.value;
    if(values.nome != undefined && values.email != undefined && values.senha != undefined && values.confirmaSenha != undefined)
         this.userService.updateUser(values.nome,values.email,values.senha,values.confirmaSenha);
  }

  excluir(){
    this.userService.deleteUser(this.originalEmail)
  }

}
