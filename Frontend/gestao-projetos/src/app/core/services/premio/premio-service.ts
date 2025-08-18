import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {PremioModel} from '../../../models/Premio.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PremioService {

  private apiUrl = `${environment.apiUrl}/premios`;

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  // GET /premios - Listar Prêmios
  listarPremios(): Observable<PremioModel[]> {
    return this.http.get<PremioModel[]>(this.apiUrl);
  }

  // GET /premios/{id} - Buscar Prêmio por ID
  buscarPremioPorId(id: number): Observable<PremioModel> {
    return this.http.get<PremioModel>(`${this.apiUrl}/${id}`);
  }

  // POST /premios - Criar Prêmio
  criarPremio(premio: PremioModel): Observable<PremioModel> {
    return this.http.post<PremioModel>(this.apiUrl, premio, this.httpOptions);
  }

  // PUT /premios/{id} - Atualizar Prêmio
  atualizarPremio(id: number, premio: PremioModel): Observable<PremioModel> {
    return this.http.put<PremioModel>(`${this.apiUrl}/${id}`, premio, this.httpOptions);
  }

  // DELETE /premios/{id} - Deletar Prêmio
  deletarPremio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // GET /premios/cronograma/{cronogramaId} - Listar Prêmios por Cronograma
  listarPremiosPorCronograma(cronogramaId: number): Observable<PremioModel[]> {
    return this.http.get<PremioModel[]>(`${this.apiUrl}/cronograma/${cronogramaId}`);
  }
}
