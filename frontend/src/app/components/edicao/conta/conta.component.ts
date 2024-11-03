import { Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../../services/user.service';

@Component({
  selector: 'app-conta',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './conta.component.html',
  styleUrl: './conta.component.css'
})
export class ContaComponent implements OnInit {
  
  constructor(private userService:UserService){}
  
  cadastroForm = new FormBuilder().group({
    nome: ["",[Validators.required]],
    email: ["",[Validators.required,Validators.email]],
    senha: [""],
    novaSenha:[""]
  })

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe((user)=>{
      if(user != null){
        this.cadastroForm.patchValue({nome:user?.name,email:user?.email,senha:"",novaSenha:""})
      }
      })
  }

  onSubmit(){

    let values = this.cadastroForm.value;
    if(values.nome != undefined && values.email != undefined && values.senha != undefined && values.novaSenha != undefined)
        this.userService.updateUser(values.nome,values.email,values.senha,values.novaSenha);
  }

  excluir(){
    this.userService.deleteUser();
  }
}
