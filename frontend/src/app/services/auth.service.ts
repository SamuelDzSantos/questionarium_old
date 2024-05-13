import { Injectable } from '@angular/core';
import { user_login } from '../types/user_login';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  isLogged() : boolean{
    let login = localStorage.getItem("login");
  if(login){
    let user_login = JSON.parse(login) as user_login;
    if(user_login.logged == true){
      return true;
    }else{
      return false;
    }
  }
  return false;
  }
  constructor() { }
}
