import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class Usuario {
  private API = environment.apiUrl + '/usuarios';

  constructor(private http: HttpClient) {}

  listar(): Observable<any> {
    return this.http.get(this.API);
  }

  salvar(usuario: any): Observable<any> {
    return this.http.post(this.API, usuario);
  }
}
