import {StatusModel} from './Status.model';

export interface CronogramaModel {
  id: number | null;
  // 10.10.2023
  dataInicio: string;
  // 15.10.2023
  dataFim: string;
  // Exp.: "Cronograma de Inovação"
  descricao: string;
  status: StatusModel;
  premiosId: number[];
}
