import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Avaliacao, AvaliacaoCreateRequest, AvaliacaoUpdateRequest} from '../../../models/Avaliacao.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AvaliacoesService {
  private readonly apiUrl = `${environment.apiUrl}/avaliacoes`;
  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  /**
   * Lista todas as avaliações
   * GET /avaliacoes
   */
  listarAvaliacoes(): Observable<Avaliacao[]> {
    return this.http.get<Avaliacao[]>(this.apiUrl, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Criar nova avaliação
   * POST /avaliacoes
   */
  criarAvaliacao(avaliacao: AvaliacaoCreateRequest): Observable<Avaliacao> {
    if (!this.validateAvaliacaoData(avaliacao)) {
      return throwError(() => new Error('Dados da avaliação são inválidos'));
    }
    return this.http.post<Avaliacao>(this.apiUrl, avaliacao, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Listar avaliações por projeto
   * GET /projeto/{projetoId}
   */
  listarAvaliacoesPorProjeto(projetoId: number): Observable<Avaliacao[]> {
    if (!projetoId || projetoId <= 0) {
      return throwError(() => new Error('ID do projeto é obrigatório e deve ser maior que zero'));
    }
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/projeto/${projetoId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Listar avaliações por avaliador
   * GET /avaliador/{avaliadorId}
   */
  listarAvaliacoesPorAvaliador(avaliadorId: number): Observable<Avaliacao[]> {
    if (!avaliadorId || avaliadorId <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }
    return this.http.get<Avaliacao[]>(`${this.apiUrl}/avaliador/${avaliadorId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Buscar avaliação por ID
   * GET /avaliacoes/{id}
   */
  buscarAvaliacaoPorId(id: number): Observable<Avaliacao> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID da avaliação é obrigatório e deve ser maior que zero'));
    }
    return this.http.get<Avaliacao>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Atualizar avaliação existente
   * PUT /avaliacoes/{id}
   */
  atualizarAvaliacao(id: number, avaliacao: AvaliacaoUpdateRequest): Observable<Avaliacao> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID da avaliação é obrigatório e deve ser maior que zero'));
    }
    return this.http.put<Avaliacao>(`${this.apiUrl}/${id}`, avaliacao, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Excluir avaliação
   * DELETE /avaliacoes/{id}
   */
  excluirAvaliacao(id: number): Observable<void> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID da avaliação é obrigatório e deve ser maior que zero'));
    }
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Valida dados básicos da avaliação
   */
  private validateAvaliacaoData(avaliacao: AvaliacaoCreateRequest): boolean {
    return !!(avaliacao.parecer?.trim() &&
              typeof avaliacao.nota === 'number' &&
              avaliacao.nota >= 0 &&
              avaliacao.nota <= 10 &&
              avaliacao.avaliadorId &&
              avaliacao.avaliadorId > 0 &&
              avaliacao.projetoId &&
              avaliacao.projetoId > 0);
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
          errorMessage = 'Avaliação não encontrada';
          break;
        case 409:
          errorMessage = 'Conflito - Avaliação já existe';
          break;
        case 422:
          errorMessage = 'Dados não processáveis';
          break;
        case 500:
          errorMessage = 'Erro interno do servidor';
          break;
        default:
          errorMessage = `Erro ${error.status}: ${error.message}`;
      }
    }

    console.error('Erro no AvaliacoesService:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  };
}
