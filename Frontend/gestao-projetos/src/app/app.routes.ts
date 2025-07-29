import {Routes} from '@angular/router';
import {Login} from './pages/login/login';
import {Dashboard} from './pages/dashboard/dashboard';
import {Home} from './pages/home/home';
import {Register} from './pages/register/register';
import {authGuardDashboard, authGuardHome} from './core/guards/auth-guard-dashboard';

export const routes: Routes = [
// Define as rotas da aplicação
  {path: 'home', component: Home, canActivate: [authGuardHome]},
  {path: 'login', component: Login},
  {path: 'register', component: Register},
  {path: 'dashboard', component: Dashboard, canActivate: [authGuardDashboard]},

  // Redireciona para a página de home se a rota não for encontrada
  {path: '', redirectTo: '/home', pathMatch: 'full'}
];
