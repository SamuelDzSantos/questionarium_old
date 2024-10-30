import { Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../../services/user.service';
import { ObjectValidatorService } from '../../../services/utils/objectValidatorService';
import { UserPatch } from '../../../types/dto/UserPatch';


@Component({
  selector: 'app-senha',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './senha.component.html',
  styleUrl: './senha.component.css'
})
export class SenhaComponent {

  senhaForm = this.formBuilder.group({
    senhaAtual: [''],
    novaSenha: [''],
    confirmaSenha: ['']
  })

  constructor(private formBuilder: FormBuilder, private validator: ObjectValidatorService, private userService: UserService) {
  }

  submit() {
    let values = this.senhaForm.value;
    if (this.validator.hasNonNullableProperties(values, 'senhaAtual', 'novaSenha', 'confirmaSenha')) {
      let patch: UserPatch = { password: values.senhaAtual, newPassword: values.novaSenha, confirmPassword: values.confirmaSenha }
      console.log(this.senhaForm.value)
      if (patch.name && patch.email && patch.password && patch.newPassword)
        this.userService.updateUser(patch.name, patch.email, patch.password, patch.newPassword);
    }
  }

}
