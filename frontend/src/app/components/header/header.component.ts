import { CommonModule, NgOptimizedImage } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { Observable } from 'rxjs';
import { UserData } from '../../types/dto/UserData';
import { NavigationEnd, Router, RouterModule } from '@angular/router';


@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, NgOptimizedImage, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {

  user$!: Observable<UserData | null>;
  url!: string;

  constructor(public userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    this.router.events.subscribe((val) => {
      if (val instanceof NavigationEnd) {
        this.url = val.url;
      }
    })
  }

  conta() {
    this.router.navigateByUrl("/edicao")
  }

  logo() {
    this.router.navigateByUrl("/home");
  }

  logout() {
    this.userService.logout();
  }


}
