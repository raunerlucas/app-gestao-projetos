import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';

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
          console.log("Token received:", response.token);
          localStorage.setItem('userToken', response.token);
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
