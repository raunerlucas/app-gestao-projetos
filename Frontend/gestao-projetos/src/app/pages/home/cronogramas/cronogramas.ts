import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CronogramaModel} from '../../../models/Cronograma.model';
import {STATUS_CRONOGRAMA_DESC, StatusCronogramaModel} from '../../../models/StatusCronograma.model';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {CronogramaService} from '../../../core/services/cronograma/cronograma-service';
import {finalize, Subject, takeUntil} from 'rxjs';

@Component({
  selector: 'app-cronogramas',
  imports: [
    CommonModule,
    FloatMenu
  ],
  templateUrl: './cronogramas.html',
  styleUrl: './cronogramas.css'
})
export class Cronogramas implements OnInit, OnDestroy {
  // Estado do componente
  exibirModalNovoCronograma = false;
  cronogramas: CronogramaModel[] = [];
  carregando = false;
  erro: string | null = null;

  // Subject para gerenciar unsubscribe
  private destroy$ = new Subject<void>();

  constructor(private cronogramaService: CronogramaService) {}

  ngOnInit(): void {
    this.carregarCronogramas();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // Carregar cronogramas da API
  private carregarCronogramas(): void {
    this.carregando = true;
    this.erro = null;

    this.cronogramaService.listarCronogramas()
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.carregando = false)
      )
      .subscribe({
        next: (cronogramas: CronogramaModel[]) => {
          this.cronogramas = cronogramas;
          console.log('Cronogramas carregados:', cronogramas);
        },
        error: (error) => {
          this.erro = 'Erro ao carregar cronogramas. Tente novamente.';
          console.error('Erro ao carregar cronogramas:', error);
          // Fallback para dados mockados durante desenvolvimento
          this.carregarDadosMock();
        }
      });
  }

  // Dados mock para desenvolvimento/fallback
  private carregarDadosMock(): void {
    this.cronogramas = [
      {
        id: 1,
        descricao: 'Submissão de Projetos',
        dataInicio: '2023-05-01',
        dataFim: '2023-06-30',
        status: StatusCronogramaModel.EM_ANDAMENTO
      },
      {
        id: 2,
        descricao: 'Período de Avaliação',
        dataInicio: '2023-07-01',
        dataFim: '2023-07-31',
        status: StatusCronogramaModel.NAO_INICIADO
      },
      {
        id: 3,
        descricao: 'Divulgação de Resultados',
        dataInicio: '2023-08-15',
        dataFim: '2023-08-15',
        status: StatusCronogramaModel.NAO_INICIADO
      },
      {
        id: 4,
        descricao: 'Submissão de Recursos',
        dataInicio: '2023-08-16',
        dataFim: '2023-08-20',
        status: StatusCronogramaModel.NAO_INICIADO
      },
      {
        id: 5,
        descricao: 'Resultado Final',
        dataInicio: '2023-08-25',
        dataFim: '2023-08-25',
        status: StatusCronogramaModel.NAO_INICIADO
      }
    ];
  }

  // Criar novo cronograma
  novoCronograma(): void {
    this.exibirModalNovoCronograma = true;
    console.log('Abrindo modal para novo cronograma');
  }

  // Salvar cronograma (método para ser chamado pelo formulário)
  salvarCronograma(cronograma: CronogramaModel): void {
    this.carregando = true;

    const operacao = cronograma.id ?
      this.cronogramaService.atualizarCronograma(cronograma.id, cronograma) :
      this.cronogramaService.criarCronograma(cronograma);

    operacao
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.carregando = false)
      )
      .subscribe({
        next: (cronogramaSalvo: CronogramaModel) => {
          if (cronograma.id) {
            // Atualizar cronograma existente
            const index = this.cronogramas.findIndex(c => c.id === cronograma.id);
            if (index !== -1) {
              this.cronogramas[index] = cronogramaSalvo;
            }
            console.log('Cronograma atualizado:', cronogramaSalvo);
          } else {
            // Adicionar novo cronograma
            this.cronogramas.push(cronogramaSalvo);
            console.log('Cronograma criado:', cronogramaSalvo);
          }
          this.toggleModalNovoCronograma();
        },
        error: (error) => {
          console.error('Erro ao salvar cronograma:', error);
          this.erro = 'Erro ao salvar cronograma. Tente novamente.';
        }
      });
  }

  // Editar cronograma
  editarCronograma(cronograma: CronogramaModel): void {
    console.log('Editando cronograma:', cronograma);
    // Aqui você pode abrir um modal de edição ou navegar para uma página de edição
    // Exemplo: passar o cronograma para o modal de edição
    this.exibirModalNovoCronograma = true;
  }

  // Deletar cronograma
  deletarCronograma(id: number): void {
    const cronograma = this.cronogramas.find(c => c.id === id);
    if (!cronograma) return;

    const confirmacao = confirm(`Deseja realmente excluir o cronograma "${cronograma.descricao}"?`);
    if (!confirmacao) return;

    this.carregando = true;

    this.cronogramaService.excluirCronograma(id)
      .pipe(
        takeUntil(this.destroy$),
        finalize(() => this.carregando = false)
      )
      .subscribe({
        next: () => {
          this.cronogramas = this.cronogramas.filter(c => c.id !== id);
          console.log('Cronograma excluído com sucesso');
        },
        error: (error) => {
          console.error('Erro ao excluir cronograma:', error);
          this.erro = 'Erro ao excluir cronograma. Tente novamente.';
        }
      });
  }

  // Recarregar cronogramas
  recarregarCronogramas(): void {
    this.carregarCronogramas();
  }

  // Gerenciar prêmios no cronograma
  adicionarPremio(cronogramaId: number, premioId: number): void {
    this.cronogramaService.adicionarPremioAoCronograma(cronogramaId, premioId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          console.log('Prêmio adicionado ao cronograma');
          this.recarregarCronogramas(); // Recarregar para atualizar dados
        },
        error: (error) => {
          console.error('Erro ao adicionar prêmio:', error);
        }
      });
  }

  removerPremio(cronogramaId: number, premioId: number): void {
    this.cronogramaService.removerPremioDoCronograma(cronogramaId, premioId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: () => {
          console.log('Prêmio removido do cronograma');
          this.recarregarCronogramas();
        },
        error: (error) => {
          console.error('Erro ao remover prêmio:', error);
        }
      });
  }

  // Métodos de UI helpers
  toggleModalNovoCronograma(): void {
    this.exibirModalNovoCronograma = !this.exibirModalNovoCronograma;
    this.erro = null; // Limpar erro ao fechar modal
  }

  getStatusLabel(status: StatusCronogramaModel): string {
    return STATUS_CRONOGRAMA_DESC[status];
  }

  getStatusClass(status: StatusCronogramaModel): string {
    switch (status) {
      case StatusCronogramaModel.NAO_INICIADO:
        return 'status-nao-iniciado';
      case StatusCronogramaModel.EM_ANDAMENTO:
        return 'status-em-andamento';
      case StatusCronogramaModel.CONCLUIDO:
        return 'status-concluido';
      case StatusCronogramaModel.ATRASADO:
        return 'status-atrasado';
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

  getMenuConfigForCronograma(cronograma: CronogramaModel): FloatMenuConfig {
    return {
      actions: [
        {
          label: 'Editar',
          icon: 'edit',
          color: 'primary',
          action: () => this.editarCronograma(cronograma),
          disabled: this.carregando
        },
        {
          label: 'Deletar',
          icon: 'delete',
          color: 'danger',
          action: () => this.deletarCronograma(cronograma.id!),
          disabled: this.carregando
        }
      ]
    };
  }
}
