import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {Projeto, ProjetoCreateRequest, ProjetoUpdateRequest} from '../../../models/Projeto.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjetosService {
  private readonly apiUrl = `${environment.apiUrl}/api/projetos`;
  private readonly httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  /**
   * Lista todos os projetos
   * GET /api/projetos
   */
  listarProjetos(): Observable<Projeto[]> {
    return this.http.get<Projeto[]>(this.apiUrl, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Busca projeto por ID
   * GET /api/projetos/{id}
   */
  buscarProjetoPorId(id: number): Observable<Projeto> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do projeto é obrigatório e deve ser maior que zero'));
    }
    return this.http.get<Projeto>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Criar novo projeto
   * POST /api/projetos
   */
  criarProjeto(projeto: ProjetoCreateRequest): Observable<Projeto> {
    if (!this.validateProjetoData(projeto)) {
      return throwError(() => new Error('Dados do projeto são inválidos'));
    }
    return this.http.post<Projeto>(this.apiUrl, projeto, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Atualizar projeto existente
   * PUT /api/projetos/{id}
   */
  atualizarProjeto(id: number, projeto: ProjetoUpdateRequest): Observable<Projeto> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do projeto é obrigatório e deve ser maior que zero'));
    }
    return this.http.put<Projeto>(`${this.apiUrl}/${id}`, projeto, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Excluir projeto
   * DELETE /api/projetos/{id}
   */
  excluirProjeto(id: number): Observable<void> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do projeto é obrigatório e deve ser maior que zero'));
    }
    return this.http.delete<void>(`${this.apiUrl}/${id}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Adicionar avaliação ao projeto
   * POST /api/projetos/{projetoId}/avaliacoes/{avaliacaoId}
   */
  adicionarAvaliacao(projetoId: number, avaliacaoId: number): Observable<void> {
    if (!projetoId || projetoId <= 0 || !avaliacaoId || avaliacaoId <= 0) {
      return throwError(() => new Error('IDs do projeto e avaliação são obrigatórios'));
    }
    return this.http.post<void>(`${this.apiUrl}/${projetoId}/avaliacoes/${avaliacaoId}`, {}, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Remover avaliação do projeto
   * DELETE /api/projetos/{projetoId}/avaliacoes/{avaliacaoId}
   */
  removerAvaliacao(projetoId: number, avaliacaoId: number): Observable<void> {
    if (!projetoId || projetoId <= 0 || !avaliacaoId || avaliacaoId <= 0) {
      return throwError(() => new Error('IDs do projeto e avaliação são obrigatórios'));
    }
    return this.http.delete<void>(`${this.apiUrl}/${projetoId}/avaliacoes/${avaliacaoId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Adicionar autor ao projeto
   * POST /api/projetos/{projetoId}/autores/{autorId}
   */
  adicionarAutor(projetoId: number, autorId: number): Observable<void> {
    if (!projetoId || projetoId <= 0 || !autorId || autorId <= 0) {
      return throwError(() => new Error('IDs do projeto e autor são obrigatórios'));
    }
    return this.http.post<void>(`${this.apiUrl}/${projetoId}/autores/${autorId}`, {}, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Remover autor do projeto
   * DELETE /api/projetos/{projetoId}/autores/{autorId}
   */
  removerAutor(projetoId: number, autorId: number): Observable<void> {
    if (!projetoId || projetoId <= 0 || !autorId || autorId <= 0) {
      return throwError(() => new Error('IDs do projeto e autor são obrigatórios'));
    }
    return this.http.delete<void>(`${this.apiUrl}/${projetoId}/autores/${autorId}`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Listar projetos vencedores
   * GET /api/projetos/vencedores
   */
  listarProjetosVencedores(): Observable<Projeto[]> {
    return this.http.get<Projeto[]>(`${this.apiUrl}/vencedores`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Listar projetos sem avaliações
   * GET /api/projetos/sem-avaliacoes
   */
  listarProjetosSemAvaliacoes(): Observable<Projeto[]> {
    return this.http.get<Projeto[]>(`${this.apiUrl}/sem-avaliacoes`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Listar projetos com avaliações
   * GET /api/projetos/com-avaliacoes
   */
  listarProjetosComAvaliacoes(): Observable<Projeto[]> {
    return this.http.get<Projeto[]>(`${this.apiUrl}/com-avaliacoes`, this.httpOptions)
      .pipe(catchError(this.handleError));
  }

  /**
   * Valida dados básicos do projeto
   */
  private validateProjetoData(projeto: ProjetoCreateRequest): boolean {
    return !!(projeto.titulo?.trim() &&
              projeto.resumo?.trim() &&
              projeto.areaTematica?.trim() &&
              Array.isArray(projeto.autores) &&
              projeto.autores.length > 0);
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
          errorMessage = 'Projeto não encontrado';
          break;
        case 409:
          errorMessage = 'Conflito - Projeto já existe ou operação inválida';
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

    console.error('Erro no ProjetosService:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  };
}
