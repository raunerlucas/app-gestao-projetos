import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CronogramaModel} from '../../../models/Cronograma.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CronogramaService {

  private apiUrl = `${environment.apiUrl}/cronogramas`;

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  // GET /cronogramas - Listar Cronogramas
  listarCronogramas(): Observable<CronogramaModel[]> {
    return this.http.get<CronogramaModel[]>(this.apiUrl);
  }

  // GET /cronogramas/{id} - Buscar Cronograma por ID
  buscarCronogramaPorId(id: number): Observable<CronogramaModel> {
    return this.http.get<CronogramaModel>(`${this.apiUrl}/${id}`);
  }

  // POST /cronogramas - Criar Cronograma
  criarCronograma(cronograma: CronogramaModel): Observable<CronogramaModel> {
    return this.http.post<CronogramaModel>(this.apiUrl, cronograma, this.httpOptions);
  }

  // PUT /cronogramas/{id} - Atualizar Cronograma
  atualizarCronograma(id: number, cronograma: CronogramaModel): Observable<CronogramaModel> {
    return this.http.put<CronogramaModel>(`${this.apiUrl}/${id}`, cronograma, this.httpOptions);
  }

  // DELETE /cronogramas/{id} - Excluir Cronograma
  excluirCronograma(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // POST /cronogramas/{cronogramaId}/premios - Adicionar Prêmio ao Cronograma
  adicionarPremioAoCronograma(cronogramaId: number, premioId: number): Observable<void> {
    const body = { premioId };
    return this.http.post<void>(`${this.apiUrl}/${cronogramaId}/premios`, body, this.httpOptions);
  }

  // DELETE /cronogramas/{cronogramaId}/premios/{premioId} - Remover Prêmio do Cronograma
  removerPremioDoCronograma(cronogramaId: number, premioId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${cronogramaId}/premios/${premioId}`);
  }
}
