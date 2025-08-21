export interface AvaliadorModel {
  id?: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  endereco?: string;
  instituicao?: string;
  lattes?: string;
  areaEspecializacao: string[];
  titulacao: TitulacaoAvaliador;
  experienciaAnos: number;
  certificacoes?: string[];
  dataCadastro?: Date;
  ativo?: boolean;
  disponivel?: boolean;
}

export enum TitulacaoAvaliador {
  GRADUACAO = 'GRADUACAO',
  ESPECIALIZACAO = 'ESPECIALIZACAO',
  MESTRADO = 'MESTRADO',
  DOUTORADO = 'DOUTORADO',
  POS_DOUTORADO = 'POS_DOUTORADO'
}

export interface AvaliadorCreateRequest {
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  endereco?: string;
  instituicao?: string;
  lattes?: string;
  areaEspecializacao: string[];
  titulacao: TitulacaoAvaliador;
  experienciaAnos: number;
  certificacoes?: string[];
}

export interface AvaliadorUpdateRequest {
  nome?: string;
  cpf?: string;
  email?: string;
  telefone?: string;
  endereco?: string;
  instituicao?: string;
  lattes?: string;
  areaEspecializacao?: string[];
  titulacao?: TitulacaoAvaliador;
  experienciaAnos?: number;
  certificacoes?: string[];
  ativo?: boolean;
  disponivel?: boolean;
}
