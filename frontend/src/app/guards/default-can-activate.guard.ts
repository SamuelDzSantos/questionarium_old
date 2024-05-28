import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { UserService } from '../services/user.service';
import { catchError, map, of, withLatestFrom } from 'rxjs';

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {
    let router = inject(Router)
    let userService = inject(UserService)
    userService.user$.subscribe((value)=>{console.log(value)})
    return userService.getCurrentUser().pipe(
        map((user)=>{
        if(route.url.toString() == "login"){
            return user == null ? true : router.createUrlTree(["home"])
        }else{
            console.log(user);
            return user == null ? router.createUrlTree(["login"]) : true;
        }
    }))
};
