import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { Observable } from 'rxjs';
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

  constructor(private userService:UserService){}
  
  ngOnInit(): void {
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
}
