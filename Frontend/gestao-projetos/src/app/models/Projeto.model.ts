export enum StatusProjeto {
  PENDENTE = 'PENDENTE',
  EM_AVALIACAO = 'EM_AVALIACAO',
  AVALIADO = 'AVALIADO'
}

export interface Projeto {
  id: number;
  titulo: string;
  areaTematica: string;
  dataEnvio: Date;
  autores: string[];
  status: StatusProjeto;
  nota?: number; // Apenas para projetos avaliados
  descricao?: string;
  arquivo?: string;
}

export interface ProjetoCreateRequest {
  titulo: string;
  areaTematica: string;
  autores: string[];
  descricao?: string;
  arquivo?: string;
}

export interface ProjetoUpdateRequest {
  titulo?: string;
  areaTematica?: string;
  autores?: string[];
  descricao?: string;
  arquivo?: string;
}
