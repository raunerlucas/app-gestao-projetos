import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {catchError, map} from 'rxjs/operators';
import {of} from 'rxjs';

interface SessionDataModel {
  token: string;
  username: string;
  expiresAt: Date;
}


@Injectable({
  providedIn: 'root'
})
export class Auth {
  USER_SESSION_KEY = 'userSession';

  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    return this.http.post<any>(`${environment.apiUrl}/auth/login`, {username, password})
      .pipe(
        map(response => {
          const sessionData: SessionDataModel = {
            token: response.token,
            username: username,
            // Define expiração para 2 horas (em milissegundos)
            expiresAt: new Date(new Date().getTime() + 2 * 60 * 60 * 1000)
          };
          sessionStorage.setItem(this.USER_SESSION_KEY, JSON.stringify(sessionData));
          return true;
        }),
        catchError(error => {
          console.error('Login failed', error);
          return of(false);
        })
      );
  }

  logout() {
    // [ ] TODO Implement logout functionality in the backend
    return this.http.post(`${environment.apiUrl}/auth/logout`, {}).subscribe(
      () => {
        localStorage.removeItem('userToken');
        console.log('Logout successful');
      },
      (error) => {
        console.error('Logout failed', error);
      }
    );
  }

  isAuthenticated(): boolean {
    let ss = sessionStorage.getItem(this.USER_SESSION_KEY);
    if (!ss) {
      return false
    }
    let token: string = JSON.parse(ss).token;
    const tokenValid = this.http?.post(`${environment.apiUrl}/auth/validate`, {token})
      .subscribe({
        next: (response: any) => {
          console.log('Token is valid', response);
          return true;
        },
        error: (error) => {
          console.error('Login failed', error);
          return false;
        }
      });
    return !!tokenValid;
  }
}
