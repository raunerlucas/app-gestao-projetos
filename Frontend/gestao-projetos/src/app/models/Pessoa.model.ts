export enum TipoPessoa {
  AUTOR = 'AUTOR',
  AVALIADOR = 'AVALIADOR'
}

export interface Pessoa {
  id: number;
  nome: string;
  cpf: string;
  telefone: string;
  email: string;
  tipo: TipoPessoa;
  projetosIds?: number[]; // Apenas para autores
}

export interface PessoaCreateRequest {
  nome: string;
  cpf: string;
  telefone: string;
  email: string;
  tipo: TipoPessoa;
  projetosIds?: number[];
}

export interface PessoaUpdateRequest {
  nome?: string;
  cpf?: string;
  telefone?: string;
  email?: string;
  projetosIds?: number[];
}
