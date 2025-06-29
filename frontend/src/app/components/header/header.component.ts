import { Component } from '@angular/core';
import { combineLatest, filter, map, Observable, startWith } from 'rxjs';
import { UserInfo } from '../../interfaces/user/user-info.data';
import { UserService } from '../../services/user.service';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { CommonModule, NgOptimizedImage } from '@angular/common';

enum PageTypes {
  MainPageLogged = "1",
  MainPageUnlogged = "2",
  LogoOnly = "3",
  DefaultLogged = "4"
}


@Component({
  selector: 'app-header',
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  user$!: Observable<UserInfo | null>;
  changes$!: Observable<[UserInfo | null, String]>;
  PageTypes = PageTypes;
  constructor(public userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.user$ = this.userService.getCurrentUser();
    // Utiliza um pipe que combina os resultados mais recentes de usuário e rota. Filtra a rota e define um valor inicial de / para garantir que sempre existe pelo menos um valor emitido.
    this.changes$ = combineLatest([this.user$, this.router.events.pipe(filter((event) => {
      return event instanceof NavigationEnd
    }))
      .pipe(map((navigation) => navigation.url))
      .pipe(startWith(window.location.pathname))
    ]);
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

  determinePage(changes: [UserInfo | null, String]): PageTypes {

    let user = changes[0]
    let url = changes[1];
    console.log(user)
    console.log(url)
    if (url == "/login" || url == "/cadastro") {
      return PageTypes.LogoOnly;
    }
    if (url == "/" && user != null)
      return PageTypes.MainPageLogged
    if (url == "/" && user == null)
      return PageTypes.MainPageUnlogged
    return PageTypes.DefaultLogged;
  }
}
