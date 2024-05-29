import { inject } from '@angular/core';
import { CanActivateFn, Router, UrlTree } from '@angular/router';
import { UserService } from '../services/user.service';
import { catchError, last, map, of, take, withLatestFrom } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { LoggedUser } from '../types/dto/LoggedUser';

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {
    let router = inject(Router)
    let userService = inject(UserService)
    let http = inject(HttpClient)
    return http.get<LoggedUser>("http://localhost:8080/api/user")
        .pipe(
        catchError((err)=>{return of(null)}),
        map((user)=>{
        console.log("User: " + user?.username + " Path: " + route.url.toString());
        if(route.url.toString() == "login" || route.url.toString() == "cadastro"){
            return user == null ? true : router.createUrlTree(["home"])
        }else{
            return user == null ? router.createUrlTree(["login"]) : true;
        }})
    )
};
