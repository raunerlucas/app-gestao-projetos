export interface AutorModel {
  id: number;
  nome: string;
  cpf: string;
  telefone: string;
  email: string;
  projetosIds: number[];
}

export interface AutorCreateRequest {
  nome: string;
  cpf: string;
  telefone: string;
  email: string;
  projetosIds: number[];
}

export interface AutorUpdateRequest {
  nome?: string;
  cpf?: string;
  telefone?: string;
  email?: string;
  projetosIds?: number[];
}
