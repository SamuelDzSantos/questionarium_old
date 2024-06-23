import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, ReplaySubject, catchError, map, of } from 'rxjs';
import { LoginResult } from '../types/dto/LoginResult';
import { UserData } from '../types/dto/UserData';
import { LoginForm } from '../types/dto/LoginForm';
import { RegisterForm } from '../types/dto/RegisterForm';
import { UserPatch } from '../types/dto/UpdatedUserForm';
import { env } from '../enviroments/enviroment';
import { LocalStorageService } from './localStorageService';

@Injectable({
  providedIn: "root"
})
export class UserService {

  private url = env.baseUrl

  user$ = new BehaviorSubject<UserData | null>(null);

  constructor(private http : HttpClient,private router:Router,private localStorageService : LocalStorageService) {

  }

  getCurrentUser(){
    return this.user$;
  }

  setCurrentUser(user:UserData | null){
    this.user$.next(user);
  }

  public initialize(){
    return this.http.get<UserData>(`${this.url}/users/current`).pipe(
      map((user)=>{
        this.setCurrentUser(user)
      }),
      catchError(()=>{
        this.setCurrentUser(null);
        this.localStorageService.clearUserToken();
        return of()
      }));
  }


  public async login(login: string, senha: string) {
    
    let form : LoginForm = {"email": login,"password" : senha};
    
    this.localStorageService.clearUserToken();
    
    this.http.post<LoginResult>(`${this.url}/login`,form,{observe:"body"}).subscribe({
      next:(result)=>{
        this.localStorageService.setUserToken(result.token);
        this.setCurrentUser({id:result.user.id,name:result.user.name,email:result.user.email} as UserData)
        this.router.navigateByUrl("/home")
        },
      error:(err : HttpErrorResponse)=>{this.handleLoggingError(err)}
    })

  }

  public logout(){
  
    this.localStorageService.clearUserToken();
  
    this.setCurrentUser(null);
  
    this.router.navigateByUrl("/login");
  
  }

  public cadastrar(email:string,nome:string,senha:string){
    
    let form : RegisterForm = {email:email,name:nome,password:senha}
    
    this.http.post<UserData>(`${this.url}/register`,form,{observe:"body"}).subscribe({
      next:()=>{this.router.navigate([""])},
      error:(err : HttpErrorResponse) =>{this.handleCadastroError(err)}
    })
  
  }

  public updateUser(nome:string,email:string,senha:string,novaSenha:string){
    
    let form : UserPatch = {
      name:nome,
      email:email,
      password:senha == "" ? null : senha,
      newPassword:novaSenha == "" ? null : novaSenha
    }

    let subject = this.getCurrentUser().subscribe((user)=>{
      this.http.patch<LoginResult>(`${this.url}/users/${user?.id}`,form).subscribe({
        next:(result)=>{
          subject.unsubscribe();
          this.localStorageService.setUserToken(result.token);
          this.setCurrentUser(result.user);
          this.router.navigate(["/home"])
        },
        error:(err:HttpErrorResponse)=>{this.handleUpdateError(err);subject.unsubscribe()}
      })  
    })

  }

  public deleteUser(){
    
    let subject = this.getCurrentUser().subscribe((user)=>{
      this.http.delete(`${this.url}/users/${user?.id}`).subscribe({
      next:()=>{
        subject.unsubscribe()
        this.localStorageService.clearUserToken();
        this.setCurrentUser(null);
        this.router.navigateByUrl("/login")
      },
      error:(err:HttpErrorResponse)=>{this.handleDeleteError(err);subject.unsubscribe()}
      })
    })

  }

private handleLoggingError(err:HttpErrorResponse){
      if(err.status == 0){
        alert("Erro de conexão!");
      }else{
        alert("Email ou senha incorretos!");
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

private handleDeleteError(err:HttpErrorResponse){
  alert("Erro ao deletar!");
}

}

