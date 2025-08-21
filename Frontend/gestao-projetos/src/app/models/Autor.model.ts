export interface AutorModel {
  id?: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  endereco?: string;
  instituicao?: string;
  lattes?: string;
  biografia?: string;
  dataCadastro?: Date;
  ativo?: boolean;
}

export interface AutorCreateRequest {
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  endereco?: string;
  instituicao?: string;
  lattes?: string;
  biografia?: string;
}

export interface AutorUpdateRequest {
  nome?: string;
  cpf?: string;
  email?: string;
  telefone?: string;
  endereco?: string;
  instituicao?: string;
  lattes?: string;
  biografia?: string;
  ativo?: boolean;
}
