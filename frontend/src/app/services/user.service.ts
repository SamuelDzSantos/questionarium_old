import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Router } from '@angular/router';
import { BehaviorSubject, Observable, Observer, PartialObserver, ReplaySubject, Subject, catchError, delayWhen, firstValueFrom, map, of, skip } from 'rxjs';
import { LoginForm } from '../types/dto/LoginForm';
import { LoggedUser } from '../types/dto/LoggedUser';
import { RegisterForm } from '../types/dto/RegisterForm';
import { UpdatedUserForm } from '../types/dto/UpdatedUserForm';
import { LogResult } from '../types/dto/LogResult';

@Injectable({
  providedIn: "root"
})
export class UserService {

  private url = "http://localhost:8080/api/"

  user$ = new ReplaySubject<LoggedUser | null>

  getCurrentUser(){
    return this.user$.asObservable();
  }

  setCurrentUser(user:LoggedUser | null){
    this.user$.next(user);
  }

  public initialize(){
    return this.http.get<LoggedUser>("http://localhost:8080/api/user").pipe(
      map((user)=>{
        this.setCurrentUser(user)
      }),
      catchError(()=>{
        this.setCurrentUser(null);
        return of(null)}));
  }

  constructor(private http : HttpClient,private router:Router) {

  }

  public async login(login: string, senha: string) {
    let form : LoginForm = {"email": login,"password" : senha};
    localStorage.removeItem("token");
    this.http.post<LogResult>(`${this.url}login`,form,{observe:"body"}).subscribe({
      next:(user)=>{
        localStorage.setItem("token",user.token);
        this.setCurrentUser({username:user.username,email:user.email} as LoggedUser)
        this.router.navigateByUrl("/home")
        },
      error:(err : HttpErrorResponse)=>{this.handleLoggingError(err)}
    })
  }

  public logout(){
    localStorage.removeItem("token");
    this.setCurrentUser(null);
    this.router.navigateByUrl("/login");
  }

  public cadastrar(email:string,nome:string,senha:string){
    let form : RegisterForm = {email:email,name:nome,password:senha}
    this.http.post(`${this.url}register`,form,{responseType:"text",observe:"body"}).subscribe({
      next:()=>{this.router.navigate([""])},
      error:(err : HttpErrorResponse) =>{this.handleCadastroError(err)}
    })
  }

  public updateUser(nome:string,email:string,senha:string,confirmaSenha:string){
    let form : UpdatedUserForm = {name:nome,email:email,password:senha,confirmPassword:confirmaSenha}
    this.http.post(`${this.url}update/user`,form,{responseType:"text"}).subscribe({
      next:()=>{this.router.navigate(["/home"])},
      error:(err:HttpErrorResponse)=>{this.handleUpdateError(err)}
    })
  }

private handleLoggingError(err:HttpErrorResponse){
      if(err.status == 0){
        alert("Erro de conexão!");
      }else{
        alert("Usuário ou senha incorretos!");
      }
 }

private handleCadastroError(err:HttpErrorResponse){
  if(err.status == 0){
    alert("Erro de conexão!");
  } 
}
private handleUpdateError(err:HttpErrorResponse){
  if(err.status == 0){
    alert("Erro de conexão!");
  } 
}
}

