import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AvaliadorCreateRequest, AvaliadorModel, AvaliadorUpdateRequest} from '../../../models/Avaliador.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AvaliadorService {
  private readonly apiUrl = `${environment.apiUrl}/avaliadores`;
  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  /**
   * Lista todos os avaliadores
   * GET /avaliadores
   */
  listarAvaliadores(): Observable<AvaliadorModel[]> {
    return this.http.get<AvaliadorModel[]>(this.apiUrl, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Busca avaliador por ID
   * GET /avaliadores/{id}
   */
  buscarAvaliadorPorId(id: number): Observable<AvaliadorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    return this.http.get<AvaliadorModel>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Cria novo avaliador
   * POST /avaliadores
   */
  criarAvaliador(avaliador: AvaliadorCreateRequest): Observable<AvaliadorModel> {
    if (!this.validateAvaliadorData(avaliador)) {
      return throwError(() => new Error('Dados do avaliador são inválidos'));
    }

    return this.http.post<AvaliadorModel>(this.apiUrl, avaliador, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Atualiza avaliador existente
   * PUT /avaliadores/{id}
   */
  atualizarAvaliador(id: number, avaliador: AvaliadorUpdateRequest): Observable<AvaliadorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    return this.http.put<AvaliadorModel>(`${this.apiUrl}/${id}`, avaliador, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Remove avaliador
   * DELETE /avaliadores/{id}
   */
  deletarAvaliador(id: number): Observable<void> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Valida dados básicos do avaliador
   */
  private validateAvaliadorData(avaliador: AvaliadorCreateRequest): boolean {
    return !!(avaliador.nome?.trim() &&
      avaliador.cpf?.trim() &&
      avaliador.telefone?.trim() &&
      avaliador.email?.trim());
  }

  /**
   * Trata erros HTTP
   */
  private handleError = (error: any): Observable<never> => {
    let errorMessage = 'Erro desconhecido';

    if (error.error instanceof ErrorEvent) {
      // Erro do lado do cliente
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      // Erro do lado do servidor
      errorMessage = `Erro ${error.status}: ${error.message}`;
    }

    console.error('Erro no AvaliadorService:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  };
}
