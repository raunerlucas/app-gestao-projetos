import {Routes} from '@angular/router';
import {Login} from './pages/login/login';
import {Dashboard} from './pages/dashboard/dashboard';
import {Home} from './pages/home/home';
import {Register} from './pages/register/register';

export const routes: Routes = [
// Define as rotas da aplicação
  {path: 'home', component: Home},
  {path: 'login', component: Login},
  {path: 'register', component: Register},
  {path: 'dashboard', component: Dashboard},

  // Redireciona para a página de login se a rota não for encontrada
  {path: '', redirectTo: '/home', pathMatch: 'full'}
];
