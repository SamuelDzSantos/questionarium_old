import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Observable } from 'rxjs';
import { UserService } from '../../services/user.service';
import { UserData } from '../../types/dto/UserData';

@Component({
  selector: 'app-edicao-perfil',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './edicao-perfil.component.html',
  styleUrl: './edicao-perfil.component.css'
})
export class EdicaoPerfilComponent implements OnInit {

  user$!: Observable<UserData | null> 

  constructor(private userService:UserService){}
  
  ngOnInit(): void {
       this.userService.getCurrentUser().subscribe((user)=>{
        if(user != null){
          this.cadastroForm.patchValue({nome:user?.name,email:user?.email,senha:"",novaSenha:""})
        }
        })
    }

  cadastroForm = new FormBuilder().group({
    nome: ["",[Validators.required]],
    email: ["",[Validators.required,Validators.email]],
    senha: [""],
    novaSenha:[""]
  })


  onSubmit(){

    let values = this.cadastroForm.value;
    if(values.nome != undefined && values.email != undefined && values.senha != undefined && values.novaSenha != undefined)
        this.userService.updateUser(values.nome,values.email,values.senha,values.novaSenha);
  }

  excluir(){
    this.userService.deleteUser();
  }

}
