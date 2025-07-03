import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-redefinir-senha',
  imports: [ReactiveFormsModule],
  templateUrl: './redefinir-senha.html',
  styleUrl: './redefinir-senha.css'
})
export class RedefinirSenha implements OnInit {

  @Output() closeModalEvent = new EventEmitter<void>();
  @Input() token!: string;

  emailForm!: FormGroup;
  formHidden = false;
  linkHidden = true;
  validado = false;
  timeLeft: number = 10;
  interval: any;


  constructor(private formBuilder: FormBuilder, private userService: UserService) {
    this.formHidden = false;
  }


  ngOnInit(): void {
    if (this.token && this.token != '') {
      this.userService.validateToken(this.token).subscribe((result) => {
        if (result) {
          console.log("OPA")
          console.log(result)
          this.validado = true;
        }

      });
    }
    this.formHidden = false;
    this.emailForm = this.formBuilder.group({
      email: ['']
    })
  }



  startTimer() {
    this.interval = setInterval(() => {
      if (this.timeLeft > 0) {
        this.timeLeft--;
      } else {
        this.stopTimer();
        // Add any actions to perform when the timer reaches 0
        console.log('Timer finished!');
      }
    }, 1000); // Update every 1000ms (1 second)
  }

  stopTimer() {
    clearInterval(this.interval);
  }


  submitEmail() {
    this.startTimer();
    let email: string = this.emailForm.value.email as string;

    console.log(this.emailForm.value);
    this.formHidden = true;
    this.linkHidden = false;
    this.userService.resetPassword(email).subscribe();
  }


  enviar() {

    let email: string = this.emailForm.value.email as string;
    if (this.timeLeft == 0) {
      this.userService.resetPassword(email).subscribe();
    }
    this.timeLeft = 10
    this.startTimer()

  }

  cancel() {
    this.closeModalEvent.emit()
  }

  closeModal() {
    this.formHidden = false;
    this.linkHidden = true;
    this.closeModalEvent.emit()
  }
}
