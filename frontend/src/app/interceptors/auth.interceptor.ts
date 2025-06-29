import { HttpInterceptorFn } from '@angular/common/http';
import { LocalStorageService } from '../services/local-storage.service';
import { inject } from '@angular/core';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  let localStorageService = inject(LocalStorageService)
  const token = localStorageService.getUserToken();
  console.log("Token adicionado: " + token);
  if (token != null || token != undefined) {
    const dupReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(dupReq);
  } else {
    return next(req);
  }
};
