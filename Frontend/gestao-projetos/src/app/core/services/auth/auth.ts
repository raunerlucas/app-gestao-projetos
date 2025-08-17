import {Inject, Injectable, PLATFORM_ID} from '@angular/core';
import {isPlatformBrowser} from '@angular/common';
import {environment} from '../../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {of} from 'rxjs';
import {SessionDataModel} from "../../../models/UserSessionModel";

@Injectable({
  providedIn: 'root'
})
export class Auth {
  USER_SESSION_KEY = 'userSession';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
  }

  login(username: string, password: string) {
    this.forceloginteste(username); //TODO: Apenas para teste, remover depois

    return this.http.post<any>(`${environment.apiUrl}/auth/login`, {username, password})
      .pipe(
        map(response => {
          if (isPlatformBrowser(this.platformId)) {
            const sessionData: SessionDataModel = {
              token: response.token,
              username: username,
              expiresAt: new Date(new Date().getTime() + 2 * 60 * 60 * 1000)
            };
            sessionStorage.setItem(this.USER_SESSION_KEY, JSON.stringify(sessionData));
          }
          return true;
        }),
        catchError(error => {
          console.error('Login failed', error);
          return of(false);
        })
      );
  }

  logout() {
    if (isPlatformBrowser(this.platformId)) {
      sessionStorage.removeItem(this.USER_SESSION_KEY);
    }
    console.log('User logged out');
  }

  isAuthenticated(): boolean {
    if (!isPlatformBrowser(this.platformId)) {
      return false;
    }
    try {
      const ss = sessionStorage.getItem(this.USER_SESSION_KEY);
      if (!ss) {
        console.error('Nenhuma sessão encontrada');
        return false;
      }

      const sessionData = JSON.parse(ss);
      const now = new Date();
      const expiresAt = new Date(sessionData.expiresAt);

      if (now > expiresAt) {
        console.error('Sessão expirada');
        this.logout();
        return false;
      }

      return true;
    } catch (e) {
      return false;
    }
  }

  private forceloginteste(username: string) {
    if (isPlatformBrowser(this.platformId)) {
      const sessionData: SessionDataModel = {
        token: "ESTE É UM TOKEN TESTE",
        username: username,
        expiresAt: new Date(new Date().getTime() + 2 * 60 * 60 * 1000)
      };
      sessionStorage.setItem(this.USER_SESSION_KEY, JSON.stringify(sessionData));
    }
  }
}
