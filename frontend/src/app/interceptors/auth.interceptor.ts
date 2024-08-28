import { HttpInterceptorFn } from '@angular/common/http';

// Interceptor que pega o token do local storage e adiciona o Header de Authorização antes de enviar as requisições

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem("token");
  if (token != null || token != undefined) {
    const dupReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(dupReq);
  } else {
    return next(req);
  }
};
