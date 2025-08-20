import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CronogramaModel} from '../../../models/Cronograma.model';
import {STATUS_CRONOGRAMA_DESC, StatusCronogramaModel} from '../../../models/StatusCronograma.model';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';

@Component({
  selector: 'app-cronogramas',
  imports: [
    CommonModule,
    FloatMenu
  ],
  templateUrl: './cronogramas.html',
  styleUrl: './cronogramas.css'
})
export class Cronogramas implements OnInit {
  exibirModalNovoCronograma = false;
  cronogramas: CronogramaModel[] = [
    {
      id: 1,
      descricao: 'Submissão de Projetos',
      dataInicio: '2023-05-01',
      dataFim: '2023-06-30',
      status: StatusCronogramaModel.EM_AVALIACAO
    },
    {
      id: 2,
      descricao: 'Período de Avaliação',
      dataInicio: '2023-07-01',
      dataFim: '2023-07-31',
      status: StatusCronogramaModel.PENDENTE
    },
    {
      id: 3,
      descricao: 'Divulgação de Resultados',
      dataInicio: '2023-08-15',
      dataFim: '2023-08-15',
      status: StatusCronogramaModel.PENDENTE
    },
    {
      id: 4,
      descricao: 'Submissão de Recursos',
      dataInicio: '2023-08-16',
      dataFim: '2023-08-20',
      status: StatusCronogramaModel.PENDENTE
    },
    {
      id: 5,
      descricao: 'Resultado Final',
      dataInicio: '2023-08-25',
      dataFim: '2023-08-25',
      status: StatusCronogramaModel.PENDENTE
    }
  ];

  constructor() {}

  ngOnInit(): void {
    // Implementar busca de cronogramas da API quando necessário
  }

  novoCronograma() {
    this.exibirModalNovoCronograma = true;
    console.log('Abrindo modal para novo cronograma');
  }

  editarCronograma(cronograma: CronogramaModel) {
    console.log('Editando cronograma:', cronograma);
    // Implementar lógica de edição aqui
  }

  toggleModalNovoCronograma() {
    this.exibirModalNovoCronograma = !this.exibirModalNovoCronograma;
  }

  getStatusLabel(status: StatusCronogramaModel): string {
    return STATUS_CRONOGRAMA_DESC[status];
  }

  getStatusClass(status: StatusCronogramaModel): string {
    switch (status) {
      case StatusCronogramaModel.EM_AVALIACAO:
        return 'status-em-andamento';
      case StatusCronogramaModel.PENDENTE:
        return 'status-pendente';
      case StatusCronogramaModel.AVALIADO:
        return 'status-concluido';
      case StatusCronogramaModel.CANCELADO:
        return 'status-cancelado';
      default:
        return '';
    }
  }

  formatDate(dateString: string): string {
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
  }

  getMenuConfigForCrono(cronograma: CronogramaModel): FloatMenuConfig {
    return {
      position: 'bottom-left',
      size: 'small',
      actions: [
        {
          label: 'Editar',
          icon: 'edit',
          color: 'primary',
          action: () => this.editarCronograma(cronograma)
        },
        {
          label: 'Deletar',
          icon: 'delete',
          color: 'danger',
          action: () => this.deletarCronograma(cronograma.id!)
        }
      ]
    };
  }

  private deletarCronograma(number: number) {
    console.log(`Deletando cronograma com ID: ${number}`);
  }
}
