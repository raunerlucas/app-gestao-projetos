export enum StatusCronogramaModel {
  NAO_INICIADO = 1,
  EM_ANDAMENTO = 2,
  CONCLUIDO = 3,
  ATRASADO = 4,
  CANCELADO = 5
}

export const STATUS_CRONOGRAMA_DESC = {
  [StatusCronogramaModel.NAO_INICIADO]: 'Não Iniciado',
  [StatusCronogramaModel.EM_ANDAMENTO]: 'Em Andamento',
  [StatusCronogramaModel.CONCLUIDO]: 'Concluído',
  [StatusCronogramaModel.ATRASADO]: 'Atrasado',
  [StatusCronogramaModel.CANCELADO]: 'Cancelado'
};
