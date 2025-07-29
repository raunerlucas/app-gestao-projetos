import {TestBed} from '@angular/core/testing';
import {CanActivateFn} from '@angular/router';

import {authGuardDashboard} from './auth-guard-dashboard';

describe('authGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => authGuardDashboard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
