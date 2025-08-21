export enum TipoPessoa {
  AUTOR = 'AUTOR',
  AVALIADOR = 'AVALIADOR'
}

export interface Pessoa {
  id?: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  tipo: TipoPessoa;
  dataCadastro?: Date;
  disponivel?: boolean; // Apenas para avaliadores
  projetosIds?: number[]; // Apenas para autores
}

export interface PessoaCreateRequest {
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  tipo: TipoPessoa;
  disponivel?: boolean;
  projetosIds?: number[];
}

export interface PessoaUpdateRequest {
  nome?: string;
  cpf?: string;
  email?: string;
  telefone?: string;
  disponivel?: boolean;
  projetosIds?: number[];
}
