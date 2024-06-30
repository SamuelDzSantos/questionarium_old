import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { ContaComponent } from './conta/conta.component';
import { PerfilComponent } from './perfil/perfil.component';
import { SenhaComponent } from './senha/senha.component';

@Component({
  selector: 'app-edicao',
  standalone: true,
  imports: [RouterModule,SenhaComponent,ContaComponent,PerfilComponent],
  templateUrl: './edicao.component.html',
  styleUrl: './edicao.component.css'
})
export class EdicaoComponent implements OnInit {

  url!:string;

  constructor(private router:Router){}
  
  ngOnInit(): void {
        this.url = this.router.url;
        this.router.events.subscribe((event)=>{
          if(event instanceof NavigationEnd){
                this.url = event.urlAfterRedirects || event.url;
            }
        })
    }

}
