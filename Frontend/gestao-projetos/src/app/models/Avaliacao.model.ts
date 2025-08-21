export enum StatusAvaliacao {
  PENDENTE = 'PENDENTE',
  AVALIADO = 'AVALIADO'
}

export interface ProjetoParaAvaliacao {
  id: number;
  titulo: string;
  areaTematica: string;
  autores: string[];
  prazo: Date;
  status: StatusAvaliacao;
}

export interface Avaliacao {
  id: number;
  projetoId: number;
  nota: number;
  comentarios: string;
  dataAvaliacao: Date;
  avaliadorId: number;
}
