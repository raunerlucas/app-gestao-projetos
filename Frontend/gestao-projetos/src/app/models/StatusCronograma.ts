export enum StatusCronograma {
  PENDENTE = 1,
  AVALIADO = 2,
  EM_AVALIACAO = 3,
  CANCELADO = 4
}

export const STATUS_CRONOGRAMA_DESC = {
  [StatusCronograma.PENDENTE]: 'Pendente',
  [StatusCronograma.AVALIADO]: 'Avaliado',
  [StatusCronograma.EM_AVALIACAO]: 'Em Avaliação',
  [StatusCronograma.CANCELADO]: 'Cancelado'
};
