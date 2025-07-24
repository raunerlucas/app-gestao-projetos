import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';

interface SessionData {
  token: string;
  username: string;
  expiresAt: Date;
}


@Injectable({
  providedIn: 'root'
})
export class Auth {

  constructor(private http: HttpClient) {
  }

   login(username: string, password: string): boolean {
    const userToken = this.http?.post(`${environment.apiUrl}/auth/login`, {username, password})
      .subscribe({
        next: (response: any) => {
          let sessionData: SessionData = {
            token: response.token,
            username: username,
            expiresAt: new Date(new Date().getTime() + 2)
          };
          sessionStorage.setItem('userSession', JSON.stringify(sessionData));
          return true;
        },
        error: (error) => {
          console.error('Login failed', error);
          return false;
        }
      });
    return !!userToken;
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
}
