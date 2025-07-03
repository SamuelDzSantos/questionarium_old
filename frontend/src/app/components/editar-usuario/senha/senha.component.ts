import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ObjectValidatorService } from '../../../services/object-validator.service';
import { PasswordUpdateForm } from '../../../types/user/password-update-form';
import { Router } from '@angular/router';
import { RedefinirSenha } from '../../../modal/redefinir-senha/redefinir-senha';

@Component({
  selector: 'app-senha',
  imports: [ReactiveFormsModule, RedefinirSenha],
  templateUrl: './senha.component.html',
  styleUrl: './senha.component.css'
})
export class SenhaComponent implements OnInit {

  senhaForm!: FormGroup;
  modalEnabled = false;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private validator: ObjectValidatorService, private router: Router) { }

  ngOnInit(): void {
    this.senhaForm = this.formBuilder.group({
      senhaAtual: [''],
      novaSenha: [''],
      confirmaSenha: ['']
    })
  }

  submit() {
    let values = this.senhaForm.value;
    if (this.validator.hasNonNullableProperties(values, 'senhaAtual', 'novaSenha', 'confirmaSenha')) {
      let patch: PasswordUpdateForm = { "currentPassword": values.senhaAtual, "newPassword": values.novaSenha, "confirmPassword": values.confirmaSenha }
      console.log(this.senhaForm.value)
      this.userService.updatePassword(patch).subscribe((result) => {
        if (result) {
          alert("Senha atualizada com sucesso! ");
          this.router.navigateByUrl("/edicao/conta");
        } else {
          alert("Erro ao atualizar senha!")
        }
      });
    }
  }

  public mostrarModal() {
    this.modalEnabled = true;
  }


  public fecharModal() {
    this.modalEnabled = false;
  }

}
