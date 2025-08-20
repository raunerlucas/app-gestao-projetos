import {StatusCronogramaModel} from './StatusCronograma.model';

export interface CronogramaModel {
  id: number | null;
  // 2023-10-15
  dataInicio: string;
  // 2023-12-31
  dataFim: string;
  // Exp.: "Cronograma de Inovação"
  descricao: string;
  status: StatusCronogramaModel;
  premiosId?: number[];
}
