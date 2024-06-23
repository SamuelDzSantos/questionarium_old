import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { UserService } from '../services/user.service';

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {
    
    let allowedUrls=["login","cadastro","","/","sobre","manual","blog","novidades"];

    let router = inject(Router)
    let userService = inject(UserService)
    

    return userService.getCurrentUser()
    .pipe(
        catchError(()=>{return of(null)}),
        map((user)=>{
            if(allowedUrls.indexOf(route.url.toString()) != -1)
                return user == null ? true : router.createUrlTree(["home"])
            else
                return user == null ? router.createUrlTree(["login"]) : true;
        }
    ))

};
