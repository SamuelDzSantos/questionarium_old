import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, map, of } from 'rxjs';
import { AuthResponse } from '../types/dto/AuthResponse';
import { UserData } from '../types/dto/UserData';
import { AuthRequest } from '../types/dto/AuthRequest';
import { RegistrationRequest } from '../types/dto/RegistrationRequest';
import { UserPatch } from '../types/dto/UserPatch';
import { env } from '../enviroments/enviroment';
import { LocalStorageService } from './localStorageService';

@Injectable({
  providedIn: "root"
})
export class UserService {

  private url = env.baseUrl

  user$ = new BehaviorSubject<UserData | null>(null);

  constructor(private http: HttpClient, private router: Router, private localStorageService: LocalStorageService) {

  }

  urls = {
    "addUser": `${env.baseUrl}/users`,
    "signIn": `${env.baseUrl}/users/signIn`,
    "passwordReset": `${env.baseUrl}/users/password-reset`
  }

  getCurrentUser() {
    return this.user$;
  }

  setCurrentUser(user: UserData | null) {
    this.user$.next(user);
  }

  public initialize() {
    return this.http.get<UserData>(`${this.url} /auth/current`).pipe(
      map((user) => {
        this.setCurrentUser(user)
      }),
      catchError(() => {
        this.setCurrentUser(null);
        this.localStorageService.clearUserToken();
        return of()
      }));
  }


  public async login(login: string, senha: string) {

    let form: AuthRequest = { "login": login, "password": senha };

    this.localStorageService.clearUserToken();

    this.http.post<AuthResponse>(this.urls.signIn, form, { observe: "body" }).subscribe({
      next: (result) => {
        this.localStorageService.setUserToken(result.accessToken);
        this.setCurrentUser({ id: result.userData.id, name: result.userData.name, email: result.userData.email } as UserData)
        this.router.navigateByUrl("/home")
      },
      error: (err: HttpErrorResponse) => { this.handleLoggingError(err) }
    })

  }

  public logout() {

    this.localStorageService.clearUserToken();

    this.setCurrentUser(null);

    this.router.navigateByUrl("/login");

  }

  public cadastrar(email: string, nome: string, senha: string) {

    let form: RegistrationRequest = { email: email, name: nome, password: senha, role: "USER" }

    this.http.post<UserData>(this.urls.addUser, form, { observe: "body" }).subscribe({
      next: () => { this.router.navigate([""]) },
      error: (err: HttpErrorResponse) => { this.handleCadastroError(err) }
    })

  }

  public updateUser(nome: string, email: string, senha: string, novaSenha: string) {

    let form: UserPatch = {
      name: nome,
      email: email,
      password: senha == "" ? undefined : senha,
      newPassword: novaSenha == "" ? undefined : novaSenha
    }

    let subject = this.getCurrentUser().subscribe((user) => {
      this.http.patch<AuthResponse>(`${this.url}/users/${user?.id}`, form).subscribe({
        next: (result) => {
          subject.unsubscribe();
          this.localStorageService.setUserToken(result.accessToken);
          this.setCurrentUser(result.userData);
          console.log("Chegou aqui")
          this.router.navigate(["/home"])
        },
        error: (err: HttpErrorResponse) => { this.handleUpdateError(err); subject.unsubscribe() }
      })
    })

  }

  public deleteUser() {

    let subject = this.getCurrentUser().subscribe((user) => {
      this.http.delete(`${this.url}/users/${user?.id}`).subscribe({
        next: () => {
          subject.unsubscribe()
          this.localStorageService.clearUserToken();
          this.setCurrentUser(null);
          this.router.navigateByUrl("/login")
        },
        error: (err: HttpErrorResponse) => { this.handleDeleteError(err); subject.unsubscribe() }
      })
    })
  }

  public recuperarSenha(email: string) {
    let subject = this.http.post<string>(this.urls.passwordReset, {
      params: {
        "email": email
      }
    })
    return subject;
  }

  private handleLoggingError(err: HttpErrorResponse) {
    if (err.status == 0) {
      alert("Erro de conexão!");
    } else {
      alert("Email ou senha incorretos!");
    }
  }

  private handleCadastroError(err: HttpErrorResponse) {
    if (err.status == 0) {
      alert("Erro de conexão!");
    }
  }
  private handleUpdateError(err: HttpErrorResponse) {
    if (err.status == 0) {
      alert("Erro de conexão!");
    }
  }

  private handleDeleteError(err: HttpErrorResponse) {
    alert("Erro ao deletar!");
  }

}

