import {Routes} from '@angular/router';
import {Login} from './pages/login/login';
import {Dashboard} from './pages/home/dashboard/dashboard';
import {Home} from './pages/home/home';
import {Register} from './pages/register/register';
import {authGuardDashboard, authGuardFirtsPage} from './core/guards/auth-guard-dashboard';
import {Premios} from './pages/home/premios/premios';
import {First} from './pages/first/first';
import {Projetos} from './pages/home/projetos/projetos';
import {Avaliacoes} from './pages/home/avaliacoes/avaliacoes';
import {Pessoas} from './pages/home/pessoas/pessoas';
import {Cronogramas} from './pages/home/cronogramas/cronogramas';
import {Configuracoes} from './pages/home/configuracoes/configuracoes';

export const routes: Routes = [
// Define as rotas da aplicação
  {path: 'first', component: First, canActivate: [authGuardFirtsPage]},
  {path: 'login', component: Login},
  {path: 'register', component: Register},
  {
    path: 'home',
    component: Home,
    canActivate: [authGuardDashboard],
    children: [
      {path: 'dashboard', component: Dashboard},
      {path: 'premios', component: Premios},
      {path: 'projetos', component: Projetos},
      {path: 'avaliacoes', component: Avaliacoes},
      {path: 'pessoas', component: Pessoas},
      {path: 'cronogramas', component: Cronogramas},
      {path: 'configuracoes', component: Configuracoes},
      {path: '', redirectTo: 'dashboard', pathMatch: 'full'} // rota padrão
    ]
  },

  // Redireciona para a página de home se a rota não for encontrada
  {path: '', redirectTo: '/first', pathMatch: 'full'}
];
