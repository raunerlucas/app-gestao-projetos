import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AutorCreateRequest, AutorModel, AutorUpdateRequest} from '../../../models/Autor.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AutorService {

  private readonly apiUrl = `${environment.apiUrl}/autores`;

  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  /**
   * Lista todos os autores
   * GET /autores
   */
  listarAutores(): Observable<AutorModel[]> {
    return this.http.get<AutorModel[]>(this.apiUrl, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Busca autor por ID
   * GET /autores/{id}
   */
  buscarAutorPorId(id: number): Observable<AutorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do autor é obrigatório e deve ser maior que zero'));
    }

    return this.http.get<AutorModel>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Cria novo autor
   * POST /autores
   */
  criarAutor(autor: AutorCreateRequest): Observable<AutorModel> {
    if (!this.validateAutorData(autor)) {
      return throwError(() => new Error('Dados do autor são inválidos'));
    }

    return this.http.post<AutorModel>(this.apiUrl, autor, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Atualiza autor existente
   * PUT /autores/{id}
   */
  atualizarAutor(id: number, autor: AutorUpdateRequest): Observable<AutorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do autor é obrigatório e deve ser maior que zero'));
    }

    return this.http.put<AutorModel>(`${this.apiUrl}/${id}`, autor, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Remove autor
   * DELETE /autores/{id}
   */
  deletarAutor(id: number): Observable<void> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do autor é obrigatório e deve ser maior que zero'));
    }

    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Valida dados básicos do autor
   */
  private validateAutorData(autor: AutorCreateRequest): boolean {
    return !!(autor.nome?.trim() &&
              autor.cpf?.trim() &&
              autor.telefone?.trim() &&
              autor.email?.trim() &&
              Array.isArray(autor.projetosIds));
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

    console.error('Erro no AutorService:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  };
}
