import {inject} from '@angular/core';
import {CanActivateFn, Router} from '@angular/router';
import {Auth} from '../services/auth/auth';


export const authGuardDashboard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  return true // TODO: Apenas para teste, remover depois

  if (authService.isAuthenticated()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};

export const authGuardFirtsPage: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    router.navigate(['/dashboard']);
    return false;
  }
  return true;
};
