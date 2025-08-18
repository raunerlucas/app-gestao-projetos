export enum StatusCronogramaModel {
  PENDENTE = 1,
  AVALIADO = 2,
  EM_AVALIACAO = 3,
  CANCELADO = 4
}

export const STATUS_CRONOGRAMA_DESC = {
  [StatusCronogramaModel.PENDENTE]: 'Pendente',
  [StatusCronogramaModel.AVALIADO]: 'Avaliado',
  [StatusCronogramaModel.EM_AVALIACAO]: 'Em Avaliação',
  [StatusCronogramaModel.CANCELADO]: 'Cancelado'
};
