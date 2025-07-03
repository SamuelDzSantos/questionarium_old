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
      nome: [''],
      email: [''],
    })

    this.userService.getCurrentUser().subscribe((user) => {
      this.senhaForm = this.formBuilder.group({
        nome: [user?.name],
        email: [user?.email],
      })
    })

  }

  updateData() {
    console.log(this.senhaForm.value);

    let nome = this.senhaForm.value.nome;
    let email = this.senhaForm.value.email;

    this.userService.patchUser(nome, email).subscribe(() => {
      alert("Dados atualizados")
      window.location.reload();
    })



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
