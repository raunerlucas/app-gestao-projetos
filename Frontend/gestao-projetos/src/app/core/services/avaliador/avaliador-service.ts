import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {
  AvaliadorCreateRequest,
  AvaliadorModel,
  AvaliadorUpdateRequest,
  TitulacaoAvaliador
} from '../../../models/Avaliador.model';
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
   * Busca avaliadores por critérios específicos
   * GET /avaliadores/buscar
   */
  buscarAvaliadores(filtros: {
    nome?: string;
    email?: string;
    cpf?: string;
    instituicao?: string;
    areaEspecializacao?: string;
    titulacao?: TitulacaoAvaliador;
    experienciaMinima?: number;
    ativo?: boolean;
    disponivel?: boolean;
  }): Observable<AvaliadorModel[]> {
    let params = new HttpParams();

    Object.entries(filtros).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value.toString());
      }
    });

    return this.http.get<AvaliadorModel[]>(`${this.apiUrl}/buscar`, {
      ...this.httpOptions,
      params
    }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Lista avaliadores disponíveis para uma área específica
   * GET /avaliadores/disponiveis
   */
  listarAvaliadoresDisponiveis(areaEspecializacao?: string): Observable<AvaliadorModel[]> {
    let params = new HttpParams().set('disponivel', 'true');

    if (areaEspecializacao) {
      params = params.set('areaEspecializacao', areaEspecializacao);
    }

    return this.http.get<AvaliadorModel[]>(`${this.apiUrl}/disponiveis`, {
      ...this.httpOptions,
      params
    }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Altera status de disponibilidade do avaliador
   * PATCH /avaliadores/{id}/disponibilidade
   */
  alterarDisponibilidade(id: number, disponivel: boolean): Observable<AvaliadorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    return this.http.patch<AvaliadorModel>(`${this.apiUrl}/${id}/disponibilidade`,
      { disponivel }, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Ativa/desativa avaliador
   * PATCH /avaliadores/{id}/status
   */
  alterarStatusAvaliador(id: number, ativo: boolean): Observable<AvaliadorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    return this.http.patch<AvaliadorModel>(`${this.apiUrl}/${id}/status`,
      { ativo }, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Adiciona área de especialização ao avaliador
   * POST /avaliadores/{id}/areas
   */
  adicionarAreaEspecializacao(id: number, area: string): Observable<AvaliadorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    if (!area?.trim()) {
      return throwError(() => new Error('Área de especialização é obrigatória'));
    }

    return this.http.post<AvaliadorModel>(`${this.apiUrl}/${id}/areas`,
      { area: area.trim() }, this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Remove área de especialização do avaliador
   * DELETE /avaliadores/{id}/areas/{area}
   */
  removerAreaEspecializacao(id: number, area: string): Observable<AvaliadorModel> {
    if (!id || id <= 0) {
      return throwError(() => new Error('ID do avaliador é obrigatório e deve ser maior que zero'));
    }

    if (!area?.trim()) {
      return throwError(() => new Error('Área de especialização é obrigatória'));
    }

    return this.http.delete<AvaliadorModel>(`${this.apiUrl}/${id}/areas/${encodeURIComponent(area)}`,
      this.httpOptions)
      .pipe(
        catchError(this.handleError)
      );
  }

  /**
   * Valida dados básicos do avaliador
   */
  private validateAvaliadorData(avaliador: AvaliadorCreateRequest): boolean {
    return !!(avaliador.nome?.trim() &&
              avaliador.email?.trim() &&
              avaliador.cpf?.trim() &&
              avaliador.telefone?.trim() &&
              avaliador.areaEspecializacao?.length > 0 &&
              avaliador.titulacao &&
              avaliador.experienciaAnos >= 0);
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
          errorMessage = 'Avaliador não encontrado';
          break;
        case 409:
          errorMessage = 'Conflito - Avaliador já existe';
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

    console.error('Erro no AvaliadorService:', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  };
}
