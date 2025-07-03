import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ObjectValidatorService } from '../../../services/object-validator.service';
import { Router } from '@angular/router';
import { PasswordUpdateForm } from '../../../types/user/password-update-form';

@Component({
  selector: 'app-conta',
  imports: [ReactiveFormsModule],
  templateUrl: './conta.component.html',
  styleUrl: './conta.component.css'
})
export class ContaComponent {

  senhaForm!: FormGroup;
  file!: File;
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
          this.router.navigateByUrl("/edicao/perfil");
        } else {
          alert("Erro ao atualizar senha!")
        }
      });
    }
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input != null) {
      let files = input!.files;
      if (files) {
        this.file = files[0];
        console.log("Enviando imagem")
        this.userService.updateImage(this.file).subscribe();
      }
    }
  }

}
