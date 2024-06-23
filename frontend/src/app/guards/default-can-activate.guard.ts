import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { UserService } from '../services/user.service';

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {
    
    let router = inject(Router)
    let userService = inject(UserService)
    
    return userService.getCurrentUser()
    .pipe(
        catchError(()=>{return of(null)}),
        map((user)=>{
            if(route.url.toString() == "login" || route.url.toString() == "cadastro" || route.url.toString() == "")
                return user == null ? true : router.createUrlTree(["home"])
            else
                return user == null ? router.createUrlTree(["login"]) : true;
        }
    ))

};
