import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { BehaviorSubject, catchError, count, map, of, throwError } from 'rxjs';
import { UserInfo } from '../interfaces/user/user-info.data';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LocalStorageService } from './local-storage.service';
import { PasswordUpdateForm } from '../types/user/password-update-form';


type UserData = {
  "id": number,
  "name": string,
  "email": string
}

type StringWrapper = {
  "token": string
}


@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = environment.apiUrl;

  private methodUrls = {
    "addUser": `${this.apiUrl}/users`,
    "signIn": `${this.apiUrl}/auth/login`,
    "register": `${this.apiUrl}/auth/register`,
    "passwordReset": `${this.apiUrl}/users/password-reset`,
    "passwordUpdate": `${this.apiUrl}/auth/password`,
    "getUserData": `${this.apiUrl}/user/data`,
  }

  user$ = new BehaviorSubject<UserInfo | null>(null);

  constructor(private http: HttpClient, private router: Router, private localStorageService: LocalStorageService) { }

  getCurrentUser() {
    return this.user$;
  }

  setCurrentUser(user: UserInfo | null) {
    this.user$.next(user);
  }


  public initialize() {
    return this.http.get<UserInfo>(this.methodUrls.getUserData).pipe(
      map((user) => {
        this.setCurrentUser(user)
      }),
      catchError(() => {
        this.setCurrentUser(null);
        this.localStorageService.clearUserToken();
        return of()
      }));
  }

  public login(email: String, password: string) {
    this.localStorageService.clearUserToken();
    this.http.post<StringWrapper>(this.methodUrls.signIn, { "login": email, "password": password })
      .pipe(catchError((err) => {
        console.error(err)
        alert("Usuário ou senha incorretos")
        return throwError(() => new Error('Something bad happened; please try again later.'))
      }))
      .subscribe((response) => {
        console.log(response.token)
        this.localStorageService.setUserToken(response.token);
        this.http.get<UserInfo>(this.methodUrls.getUserData).subscribe((response) => {
          this.setCurrentUser({ "id": response.id, "name": response.name, "email": response.email } as UserInfo)
          this.router.navigateByUrl("")
        })
      })
  }

  public register(name: String, email: String, password: String) {
    this.http.post(this.methodUrls.register, { "name": name, "email": email, "password": password, "role": "USER" }).subscribe((response) => {
      this.router.navigateByUrl("/login")
    });
  }

  public updatePassword(patch: PasswordUpdateForm) {
    return this.http.patch<boolean>(this.methodUrls.passwordUpdate, patch);
  }

  public logout() {

    this.localStorageService.clearUserToken();
    this.setCurrentUser(null);
    this.router.navigateByUrl("/login");

  }


}
