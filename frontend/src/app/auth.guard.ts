import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from './services/auth.service';


export const authGuard: CanActivateFn = (route, state) => {
  
  let router = inject(Router);
  let authService = inject(AuthService);
  if(authService.isLogged() == false && route.url.toString() != "login")
    router.navigate(["login"])
  if(authService.isLogged() == true && route.url.toString() == "login" ){
   router.navigate([""])
  } 
  return true;
  };
