export interface AvaliadorModel {
  id: number;
  nome: string;
  cpf: string;
  telefone: string;
  email: string;
}

export interface AvaliadorCreateRequest {
  nome: string;
  cpf: string;
  telefone: string;
  email: string;
}

export interface AvaliadorUpdateRequest {
  nome?: string;
  cpf?: string;
  telefone?: string;
  email?: string;
}
