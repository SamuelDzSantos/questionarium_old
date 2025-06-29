import { Routes } from '@angular/router';
import { CadastrarComponent } from './components/cadastrar/cadastrar.component';
import { DevsComponent } from './components/devs/devs.component';
import { EdicaoComponent } from './components/editar-usuario/edicao/edicao.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { MainComponent } from './components/main/main.component';
import { CreateQuestionComponent, ViewQuestionsComponent } from './components/questions';
import { SobreComponent } from './components/sobre/sobre.component';
import { defaultCanActivateGuard } from './guards/default-can-activate.guard';
import { ListarRelatoriosComponent } from './components/relatorios/listar-relatorios/listar-relatorios';
import { VerRelatorioComponent } from './components/relatorios/ver-relatorio/ver-relatorio';

export const routes: Routes = [
    {
        path: "",
        children: [
            { path: "", component: MainComponent },
            { path: "login", component: LoginComponent },
            { path: "cadastro", component: CadastrarComponent },
            { path: "home", component: HomeComponent },
            { path: "sobre", component: SobreComponent },
            { path: "devs", component: DevsComponent },
            {
                path: "edicao",
                children: [
                    { path: "conta", component: EdicaoComponent },
                    { path: "senha", component: EdicaoComponent },
                    { path: "perfil", component: EdicaoComponent },
                    { path: "", redirectTo: "perfil", pathMatch: "prefix" }
                ]
            },
            {
                path: "questions",
                children: [
                    { path: "", component: ViewQuestionsComponent },
                    { path: "criar", component: CreateQuestionComponent },
                    { path: ":id", component: CreateQuestionComponent }
                ]
            },
            {
                path: "relatorios",
                children: [
                    { path: "", component: ListarRelatoriosComponent },
                    { path: ":id", component: VerRelatorioComponent }
                ]
            },
        ],
        canActivateChild: [defaultCanActivateGuard]
    }
];
