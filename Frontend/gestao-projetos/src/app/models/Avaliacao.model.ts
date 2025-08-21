export interface AvaliadorAvaliacao {
  id: number;
  nome: string;
  email: string;
}

export interface ProjetoAvaliacao {
  id: number;
  titulo: string;
  areaTematica: string;
}

export interface StatusAvaliacao {
  id: number;
  description: string;
}

export interface Avaliacao {
  id: number;
  parecer: string;
  nota: number;
  dataAvaliacao: string; // ISO date string
  avaliador: AvaliadorAvaliacao;
  projeto: ProjetoAvaliacao;
  status: StatusAvaliacao;
}

export interface AvaliacaoCreateRequest {
  parecer: string;
  nota: number;
  avaliadorId: number;
  projetoId: number;
  statusId?: number;
}

export interface AvaliacaoUpdateRequest {
  parecer?: string;
  nota?: number;
  avaliadorId?: number;
  projetoId?: number;
  statusId?: number;
}
