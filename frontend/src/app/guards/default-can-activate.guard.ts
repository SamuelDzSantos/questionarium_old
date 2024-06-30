import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { UserService } from '../services/user.service';

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {
    
    let freeUrls=["","/","sobre","manual","blog","novidades"]
    let unloggedOnlyUrls=["login","cadastro"]

    let router = inject(Router)
    let userService = inject(UserService)
    
    let currentUrl = route.url.toString();


    return userService.getCurrentUser()
    .pipe(
        catchError(()=>{return of(null)}),
        map((user)=>{

            if(freeUrls.indexOf(currentUrl) != -1)
                return true;
            if(unloggedOnlyUrls.indexOf(route.url.toString()) != -1)
                return user == null ? true : router.createUrlTree(["home"])
            else
                return user == null ? router.createUrlTree(["login"]) : true;
        }
    ))

};
