import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { EdicaoComponent } from './components/edicao/edicao.component';
import { defaultCanActivateGuard } from './guards/default-can-activate.guard';
import { MainComponent } from './components/main/main.component';
import { PageNotFoundComponent } from './components/page-not-found/page-not-found.component';
import { AvaliacaoComponent } from './components/avaliacao/avaliacao.component';
import { SobreComponent } from './components/sobre/sobre.component';
import { DevsComponent } from './components/devs/devs.component';
import { TurmaComponent } from './components/turma/turma.component';
import { AvaliacaoCriarComponent } from './components/avaliacao/avaliacao-criar/avaliacao-criar.component';
import { AddTurmaComponent } from './components/add-turma/add-turma.component';
import { RecuperarSenhaComponent } from './components/recuperar-senha/recuperar-senha.component';
import { CreateQuestionComponent, ViewQuestionsComponent } from './components/questions';
import { GerarQuestaoComponent } from './modal/gerar-questao/gerar-questao.component';
import { CriarCabecalhoComponent } from './modal/criar-cabecalho/criar-cabecalho.component';
import { CadastroComponent } from './components/cadastro/cadastro.component';
import { AvaliacaoAplicarComponent } from './components/avaliacao/avaliacao-aplicar/avaliacao-aplicar.component';
import { AvaliacaoAplicadasComponent } from './components/avaliacao/avaliacao-aplicadas/avaliacao-aplicadas.component';
import { ListarRelatoriosComponent } from './components/relatorios/listar-relatorios/listar-relatorios.component';
import { VerRelatorioComponent } from './components/relatorios/ver-relatorio/ver-relatorio.component';

export const routes: Routes = [
    {
        path: "",
        children: [
            { path: "", component: MainComponent },
            { path: "home", component: HomeComponent },
            { path: "login", component: LoginComponent },
            { path: "cadastro", component: CadastroComponent },
            { path: "recuperar-senha", component: RecuperarSenhaComponent },
            {
                path: "edicao",
                children: [
                    { path: "conta", component: EdicaoComponent },
                    { path: "", redirectTo: "perfil", pathMatch: "prefix" },
                    { path: "perfil", component: EdicaoComponent },
                    { path: "senha", component: EdicaoComponent }
                ]
            },
            {
                path: "avaliacao",
                children: [
                    { path: "", component: AvaliacaoComponent },
                    { path: "criar", component: AvaliacaoCriarComponent },  
                    { path: "aplicar", component: AvaliacaoAplicarComponent },
                    { path: "aplicadas", component: AvaliacaoAplicadasComponent }
                ]
            },
            {
                path: "questions",
                children: [
                    { path: "", component: ViewQuestionsComponent},
                    { path: "criar", component: CreateQuestionComponent },
                    { path: ":id", component: CreateQuestionComponent }
                ]
            },
            {
                path: "relatorios",
                children: [
                    { path: "", component: ListarRelatoriosComponent},
                    { path: ":id", component: VerRelatorioComponent }
                ]
            },
            { path: "sobre", component: SobreComponent },
            { path: "devs", component: DevsComponent },
            // { path: "turma", component: TurmaComponent },
            // { path: "addturma", component: AddTurmaComponent },
            { path: "**", component: PageNotFoundComponent },
        ],
        //canActivateChild: [defaultCanActivateGuard]
    }
];