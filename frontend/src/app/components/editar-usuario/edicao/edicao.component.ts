import { Component, OnInit } from '@angular/core';
import { SenhaComponent } from '../senha/senha.component';
import { ContaComponent } from '../conta/conta.component';
import { PerfilComponent } from '../perfil/perfil.component';
import { NavigationEnd, Router, RouterModule } from '@angular/router';
import { filter, map, Observable, startWith } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-edicao',
  imports: [SenhaComponent, ContaComponent, PerfilComponent, CommonModule, RouterModule],
  templateUrl: './edicao.component.html',
  styleUrl: './edicao.component.css'
})
export class EdicaoComponent implements OnInit {

  url$!: Observable<String>;

  constructor(private router: Router) { }

  ngOnInit(): void {

    this.url$ = this.router.events.pipe(filter((event) => event instanceof NavigationEnd)).pipe(map((navigation) => { return navigation.urlAfterRedirects || navigation.url }), startWith(this.router.url));
  }


  teste() {
    console.log("OPa")
  }


}
