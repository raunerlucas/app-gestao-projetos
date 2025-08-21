export interface AutorProjeto {
  id: number;
  nome: string;
}

export interface AvaliacaoProjeto {
  id: number;
  nota: number;
  parecer: string;
}

export interface Projeto {
  id: number;
  titulo: string;
  resumo: string;
  dataEnvio: string;
  areaTematica: string;
  autores: AutorProjeto[];
  avaliacoes: AvaliacaoProjeto[];
}

export interface ProjetoCreateRequest {
  titulo: string;
  resumo: string;
  areaTematica: string;
  autores: AutorProjeto[];
}

export interface ProjetoUpdateRequest {
  titulo?: string;
  resumo?: string;
  areaTematica?: string;
  autores?: AutorProjeto[];
}

export enum StatusProjeto {
  PENDENTE = 'PENDENTE',
  EM_AVALIACAO = 'EM_AVALIACAO',
  AVALIADO = 'AVALIADO'
}
