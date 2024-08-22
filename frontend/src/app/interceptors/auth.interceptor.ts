import { HttpInterceptorFn } from '@angular/common/http';

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
