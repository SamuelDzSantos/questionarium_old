import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-redefinir-senha',
  imports: [ReactiveFormsModule],
  templateUrl: './redefinir-senha.html',
  styleUrl: './redefinir-senha.css'
})
export class RedefinirSenha {

  @Output() closeModalEvent = new EventEmitter<void>();

  emailForm!: FormGroup;
  formHidden = false;
  linkHidden = true;

  timeLeft: number = 10;
  interval: any;


  constructor(private formBuilder: FormBuilder, private userService: UserService) {
    this.formHidden = false;
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


  ngOnInit(): void {
    this.formHidden = false;
    this.emailForm = this.formBuilder.group({
      email: ['']
    })
  }

  submitEmail() {
    this.startTimer();
    console.log(this.emailForm.value);
    this.formHidden = true;
    this.linkHidden = false;
    console.log(this.formHidden)
  }


  enviar() {

    if (this.timeLeft == 0) {
      console.log("OPa")
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
