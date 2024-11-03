import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { PasswordPatch } from '../../types/dto/PasswordPatch';
import { Router } from '@angular/router';


@Component({
  selector: 'app-recuperar-senha',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './recuperar-senha.component.html',
  styleUrl: './recuperar-senha.component.css'
})
export class RecuperarSenhaComponent {

  token = ""


  patchSenhaForm = new FormBuilder().group({
    senha: ["", [Validators.required]],
    comfirmaSenha: ["", [Validators.required]]
  })

  constructor(private userService: UserService, private router: Router) {
    this.token = localStorage.getItem("senha-token")?.toString() || "";
  }

  onSubmit() {
    let values = this.patchSenhaForm.value;

    if (values.senha != undefined && values.comfirmaSenha != undefined) {
      let patch: PasswordPatch = { "password": values.senha, "confirmPassword": values.comfirmaSenha, "token": this.token };
      this.userService.atualizarSenha(patch)
      this.router.navigateByUrl("/login")
    }

  }

}