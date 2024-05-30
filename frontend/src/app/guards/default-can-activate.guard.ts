import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { catchError, map, of } from 'rxjs';
import { UserService } from '../services/user.service';
import { LoggedUser } from '../types/dto/LoggedUser';
import { env } from '../enviroment';

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {
    let router = inject(Router)
    let userService = inject(UserService)
    let http = inject(HttpClient)
    return http.get<LoggedUser>(`${env.baseUrl}user`)
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
