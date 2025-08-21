import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
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
   * Busca autores por critérios específicos
   * GET /autores/buscar
   */
  buscarAutores(filtros: {
    nome?: string;
    email?: string;
    cpf?: string;
    instituicao?: string;
    ativo?: boolean;
  }): Observable<AutorModel[]> {
    let params = new HttpParams();

    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<AutorModel[]>(`${this.apiUrl}/buscar`, {
      ...this.httpOptions,
      params
    }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Ativa/desativa autor
   * PATCH /autores/{id}/status
   */
  alterarStatusAutor(id: number, ativo: boolean): Observable<AutorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do autor é obrigatório e deve ser maior que zero'));
    }

    return this.http.patch<AutorModel>(`${this.apiUrl}/${id}/status`, { ativo }, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Valida dados básicos do autor
   */
  private validateAutorData(autor: AutorCreateRequest): boolean {
    return !!(autor.nome?.trim() &&
              autor.email?.trim() &&
              autor.cpf?.trim() &&
              autor.telefone?.trim());
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
      switch (error.status) {
        case 400:
          errorMessage = 'Dados inválidos fornecidos';
          break;
        case 401:
          errorMessage = 'Não autorizado';
          break;
        case 403:
          errorMessage = 'Acesso negado';
          break;
        case 404:
          errorMessage = 'Autor não encontrado';
          break;
        case 409:
          errorMessage = 'Conflito - Autor já existe';
          break;
        case 500:
          errorMessage = 'Erro interno do servidor';
          break;
        default:
          errorMessage = `Erro ${error.status}: ${error.message}`;
      }
    }

    console.error('Erro no AutorService:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  };
}
