import { ApplicationConfig, inject, provideAppInitializer, Provider, provideZoneChangeDetection } from '@angular/core';
import { provideRouter, withDebugTracing } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { authInterceptor } from './interceptors/auth.interceptor';
import { UserService } from './services/user.service';

/*
 Carrega usuário antes de carregar as páginas do angular.Sem isso o Header primeiro mostra a versão não logada e depois quando o usuário é obtido mostra a versão logada.
 Com esse initializer o Header já começa com a versão logada.
*/
const GET_USER_INITIALIZER = provideAppInitializer(() => {
  const userService = inject(UserService);
  return userService.initialize();
})

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    //provideRouter(routes, withDebugTracing()),
    provideRouter(routes),
    provideHttpClient(withFetch(), withInterceptors([authInterceptor])),
    GET_USER_INITIALIZER
  ]
};

