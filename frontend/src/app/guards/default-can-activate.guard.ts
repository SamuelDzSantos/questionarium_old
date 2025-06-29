import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { catchError, map, of } from 'rxjs';
// Guard utilizado para permitir o acesso ao usuário ou redirecioná-lo com base na autenticação e no caminho.

export const defaultCanActivateGuard: CanActivateFn = (route, state) => {

  // Adicionar uma nova url a essa lista caso ela seja liberada para todos os tipos de usuário (freeUrls) ou urls que devem ser
  // apenas acessadas por usuário não logados (unloggedUrls).

  let freeUrls = ["", "/", "sobre", "manual", "blog", "novidades"]
  let unloggedOnlyUrls = ["login", "cadastro", "recuperar-senha"]

  let router = inject(Router)
  let userService = inject(UserService)

  let currentUrl = route.url.toString();
  console.log(currentUrl)
  return userService.getCurrentUser()
    .pipe(
      catchError(() => { return of(null) }),
      map((user) => {
        // Caso a url requisitada exista na lista de urls que podem ser acessadas por qualquer usuário (freeUrls) retorne sempre true e permita o acesso.
        if (freeUrls.indexOf(currentUrl) != -1)
          return true;
        // Confere url com base nas url permitidas apenas para usuários não logados , se estiver logado ( user != null) redireciona para a tela home,
        // caso contrário permite o acesso.
        if (unloggedOnlyUrls.indexOf(route.url.toString()) != -1) {
          if (user != null) {
            console.log("Usuário logado " + user.name + " tentou acessar o caminho: " + route.url.toString() + "permitido apenas para usuários não logados , redirecionando para Home!");
          }
          return user == null ? true : router.createUrlTree(["home"])
        }
        // Confere url com base nas urls restantes ( urls permitidas apenas para usuários logados) se não estiver logado redireciona para a tela de login,
        // caso constrário permite o acesso.
        else {
          if (user == null) {
            console.log("Usuário não logado tentou acessar o caminho: " + route.url.toString() + "permitido apenas para usuários logados , redirecionando para Home!");
          }
          return user == null ? router.createUrlTree(["login"]) : true;
        }
      }
      ))

};
