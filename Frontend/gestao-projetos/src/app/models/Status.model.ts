export enum StatusModel {
  PENDENTE = 1,
  AVALIADO = 2,
  EM_AVALIACAO = 3,
  CANCELADO = 4
}

export const STATUS_MODEL_DESCRIPTIONS = {
  [StatusModel.PENDENTE]: 'Pendente',
  [StatusModel.AVALIADO]: 'Avaliado',
  [StatusModel.EM_AVALIACAO]: 'Em Avaliação',
  [StatusModel.CANCELADO]: 'Cancelado'
};
