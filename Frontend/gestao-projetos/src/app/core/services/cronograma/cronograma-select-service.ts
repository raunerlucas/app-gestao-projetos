import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {CronogramaService} from './cronograma-service';
import {SearchableOption} from '../../../shared/searchable-select/searchable-select';

@Injectable({
  providedIn: 'root'
})
export class CronogramaSelectService {

  constructor(private cronogramaService: CronogramaService) {
  }

  buscarCronogramas(termo: string = ''): Observable<SearchableOption[]> {
    return this.cronogramaService.listarCronogramas().pipe(
      map(cronogramas =>
        cronogramas
          .filter(cronograma =>
            termo === '' ||
            cronograma.descricao.toLowerCase().includes(termo.toLowerCase())
          )
          .map(cronograma => ({
            id: cronograma.id,
            label: `${cronograma.descricao} (${cronograma.dataInicio} - ${cronograma.dataFim})`,
            value: cronograma.id,
            cronograma: cronograma
          }))
      )
    );
  }
}
