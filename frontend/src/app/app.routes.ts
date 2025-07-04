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
import { AvaliacaoComponent } from './components/avaliacao/avaliacao.component';
import { AvaliacaoCriarComponent } from './components/avaliacao/avaliacao-criar/avaliacao-criar.component';
import { AvaliacaoAplicadasComponent } from './components/avaliacao/avaliacao-aplicadas/avaliacao-aplicadas.component';
import { RecuperarSenha } from './components/recuperar-senha/recuperar-senha';

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
            { path: "recuperar-senha", component: RecuperarSenha },
            { path: "auth", component: LoginComponent },
            { path: "password/reset", component: RecuperarSenha },
            {
                path: "edicao",
                children: [
                    { path: "conta", component: EdicaoComponent },
                    { path: "senha", component: EdicaoComponent },
                    { path: "", redirectTo: "conta", pathMatch: "prefix" }
                ]
            },
            {
                path: "avaliacao",
                children: [
                    { path: "", component: AvaliacaoComponent },
                    { path: "criar", component: AvaliacaoCriarComponent },
                    /*   { path: "aplicar", component: AvaliacaoAplicarComponent },*/
                    { path: "aplicadas", component: AvaliacaoAplicadasComponent },
                    { path: ":id", component: AvaliacaoCriarComponent }
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
