import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  constructor() { }

  public setUserToken(userToken: string) {
    localStorage.setItem("token", userToken);
  }

  public getUserToken(): string | null {
    return localStorage.getItem("token");
  }

  public clearUserToken() {
    localStorage.removeItem("token");
  }

  public getUserId(): number | null {
    return Number(localStorage.getItem("userId"));
  }

}
