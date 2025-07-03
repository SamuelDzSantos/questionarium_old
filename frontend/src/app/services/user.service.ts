import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { BehaviorSubject, catchError, count, map, of, throwError } from 'rxjs';
import { UserInfo } from '../interfaces/user/user-info.data';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { LocalStorageService } from './local-storage.service';
import { PasswordUpdateForm } from '../types/user/password-update-form';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';




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
    "passwordReset": `${this.apiUrl}/auth/password/reset`,
    "updateTokenPassword": `${this.apiUrl}/auth/password/reset`,
    "passwordUpdate": `${this.apiUrl}/auth/password`,
    "getUserData": `${this.apiUrl}/user/data`,
    "validateEmailToken": `${this.apiUrl}/auth/email`,
    "updateImage": `${this.apiUrl}/users/upload`
  }

  user$ = new BehaviorSubject<UserInfo | null>(null);

  constructor(private http: HttpClient, private router: Router, private localStorageService: LocalStorageService, private sanitizer: DomSanitizer) { }

  getCurrentUser() {
    return this.user$;
  }

  getUserImageUrl() {
    return this.user$.pipe(
      map(user => {
        const imageUrl = this.convertToImageUrl(user!.image);
        return { imageUrl };
      })
    );
  }

  private convertToImageUrl(base64: string): SafeUrl {
    const byteCharacters = atob(base64); // decodifica base64
    const byteNumbers = new Array(byteCharacters.length).fill(0).map((_, i) => byteCharacters.charCodeAt(i));
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: 'image/png' }); // ajuste o MIME type conforme necessário
    const objectUrl = URL.createObjectURL(blob);
    return this.sanitizer.bypassSecurityTrustUrl(objectUrl);
  }

  setCurrentUser(user: UserInfo | null) {
    this.user$.next(user);
  }


  public initialize() {

    this.localStorageService.clearToken("isMultipleChoice");

    return this.http.get<UserInfo>(this.methodUrls.getUserData).pipe(
      map((user) => {
        console.log(user)
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

  public resetPassword(email: string) {
    return this.http.get(`${this.methodUrls.passwordReset}?email=${email}`);
  }

  public tokenUpdatePassword(form: PasswordUpdateForm, token: string) {
    return this.http.post<void>(`${this.methodUrls.updateTokenPassword}/${token}`, form);
  }

  public validateToken(token: string) {
    return this.http.get<boolean>(`${this.methodUrls.validateEmailToken}/${token}`);
  }

  public updatePassword(patch: PasswordUpdateForm) {
    return this.http.patch<boolean>(this.methodUrls.passwordUpdate, patch);
  }

  public updateImage(file: File) {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(this.methodUrls.updateImage, formData);
  }

  public logout() {

    this.localStorageService.clearUserToken();
    this.setCurrentUser(null);
    this.router.navigateByUrl("/login");

  }


}
