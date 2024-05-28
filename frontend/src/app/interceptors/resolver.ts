import { Injectable, inject } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, ResolveFn, RouterStateSnapshot } from "@angular/router";
import { LoggedUser } from "../types/dto/LoggedUser";
import { HttpClient } from "@angular/common/http";
import { UserService } from "../services/user.service";
import { catchError, map, of } from "rxjs";

/*

export const authResolver: ResolveFn<LoggedUser | null> = (  route: ActivatedRouteSnapshot,  state: RouterStateSnapshot,) => {
    let http = inject(HttpClient);
    return http.get<LoggedUser>("http://localhost:8080/api/user").pipe(catchError(()=>of(null)));
};
*/