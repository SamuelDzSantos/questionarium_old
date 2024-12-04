import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-cadastro',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './cadastro.component.html',
  styleUrl: './cadastro.component.css'
})
export class CadastroComponent {

  constructor(private userService:UserService){}

  cadastroForm = new FormBuilder().group({
    nome: ["",[Validators.required]],
    email: ["",[Validators.required,Validators.email]],
    senha: ["",[Validators.required]]
  })

  onSubmit(){
    let values = this.cadastroForm.value
    if(values.email != undefined && values.nome != undefined && values.senha != undefined){
      this.userService.cadastrar(values.email,values.nome,values.senha);
    }
    }

}
