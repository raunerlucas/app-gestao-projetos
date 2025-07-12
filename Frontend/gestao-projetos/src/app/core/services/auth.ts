import { Injectable } from '@angular/core';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class Auth {
  constructor(private http: HttpClient) {
  }

  login(username: string, password: string) {
    return this.http.post(`${environment.apiUrl}/auth/login`, { username, password });
  }

  logout() {
    return this.http.post(`${environment.apiUrl}/auth/logout`, {});
  }
}
