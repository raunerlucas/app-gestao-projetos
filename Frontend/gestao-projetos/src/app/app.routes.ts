import {Routes} from '@angular/router';
import {Login} from './pages/login/login';
import {Dashboard} from './pages/dashboard/dashboard';
import {Home} from './pages/home/home';
import {Register} from './pages/register/register';
import {authGuardDashboard, authGuardFirtsPage} from './core/guards/auth-guard-dashboard';
import {Premios} from './pages/premios/premios';
import {First} from './pages/first/first';

export const routes: Routes = [
// Define as rotas da aplicação
  {path: 'first', component: First, canActivate: [authGuardFirtsPage]},
  {path: 'login', component: Login},
  {path: 'register', component: Register},
  {
    path: 'dashboard',
    component: Dashboard,
    canActivate: [authGuardDashboard],
    children: [
      {path: 'premios', component: Premios},
      {path: 'projetos', component: Home}, // temporário até criar o componente
      {path: 'avaliacoes', component: Home}, // temporário até criar o componente
      {path: 'pessoas', component: Home}, // temporário até criar o componente
      {path: 'cronogramas', component: Home}, // temporário até criar o componente
      {path: 'configuracoes', component: Home}, // temporário até criar o componente
      {path: '', redirectTo: 'premios', pathMatch: 'full'} // rota padrão dentro do dashboard
    ]
  },

  // Redireciona para a página de home se a rota não for encontrada
  {path: '', redirectTo: '/first', pathMatch: 'full'}
];
