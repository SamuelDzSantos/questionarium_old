import { CommonModule, NgOptimizedImage } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Observable } from 'rxjs';
import { LoggedUser } from '../../types/dto/LoggedUser';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule,NgOptimizedImage],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit{
  
  user$!: Observable<LoggedUser | null>;

  constructor(public userService:UserService, private router : Router){}
  
  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.user$.subscribe((user)=>{
     // console.log(user);
    })
  }
  
  conta(){
    this.router.navigateByUrl("/edicaoPerfil")
  }

  logo(){
    this.router.navigateByUrl("/home");
  }

  logout(){
    this.userService.logout();
  }


}
