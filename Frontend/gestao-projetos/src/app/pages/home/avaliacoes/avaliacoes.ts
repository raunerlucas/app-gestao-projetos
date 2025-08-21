import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {of, Subject} from 'rxjs';
import {catchError, finalize, takeUntil} from 'rxjs/operators';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';
import {Avaliacao} from '../../../models/Avaliacao.model';
import {AvaliacoesService} from '../../../core/services/avaliacoes/avaliacoes-service';

@Component({
  selector: 'app-avaliacoes',
  standalone: true,
  imports: [CommonModule, FormsModule, FloatMenu],
  templateUrl: './avaliacoes.html',
  styleUrl: './avaliacoes.css'
})
export class Avaliacoes implements OnInit, OnDestroy {
  avaliacoes: Avaliacao[] = [];
  filteredAvaliacoes: Avaliacao[] = [];
  searchTerm: string = '';
  statusFilter: string = 'TODOS';
  loading: boolean = false;
  error: string | null = null;

  private destroy$ = new Subject<void>();

  // Opções do filtro de status
  statusOptions = [
    { value: 'TODOS', label: 'Todos' },
    { value: 'PENDENTE', label: 'Pendente' },
    { value: 'APROVADO', label: 'Aprovado' },
    { value: 'REJEITADO', label: 'Rejeitado' }
  ];

  constructor(private avaliacoesService: AvaliacoesService) {}

  ngOnInit(): void {
    this.loadAvaliacoes();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadAvaliacoes(): void {
    this.loading = true;
    this.error = null;

    this.avaliacoesService.listarAvaliacoes().pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao carregar avaliações:', err);
        this.error = 'Erro ao carregar avaliações. Tente novamente.';
        return of([]);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(avaliacoes => {
      this.avaliacoes = avaliacoes;
      this.applyFilters();
    });
  }

  onSearch(): void {
    this.applyFilters();
  }

  onStatusFilterChange(): void {
    this.applyFilters();
  }

  private applyFilters(): void {
    let filtered = [...this.avaliacoes];

    // Filtrar por termo de busca
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase().trim();
      filtered = filtered.filter(avaliacao =>
        avaliacao.projeto.titulo.toLowerCase().includes(term) ||
        avaliacao.projeto.areaTematica.toLowerCase().includes(term) ||
        avaliacao.avaliador.nome.toLowerCase().includes(term) ||
        avaliacao.parecer.toLowerCase().includes(term)
      );
    }

    // Filtrar por status
    if (this.statusFilter !== 'TODOS') {
      filtered = filtered.filter(avaliacao =>
        avaliacao.status.description.toLowerCase() === this.statusFilter.toLowerCase()
      );
    }

    this.filteredAvaliacoes = filtered;
  }

  getFloatMenuConfig(avaliacao: Avaliacao): FloatMenuConfig {
    const actions = [
      {
        label: 'Visualizar',
        icon: 'visibility',
        color: 'primary' as const,
        action: () => this.visualizarAvaliacao(avaliacao.id)
      },
      {
        label: 'Editar',
        icon: 'edit',
        color: 'secondary' as const,
        action: () => this.editarAvaliacao(avaliacao.id)
      },
      {
        label: 'Ver Projeto',
        icon: 'assignment',
        color: 'info' as const,
        action: () => this.verProjeto(avaliacao.projeto.id)
      },
      {
        label: 'Excluir',
        icon: 'delete',
        color: 'danger' as const,
        action: () => this.excluirAvaliacao(avaliacao.id)
      }
    ];

    return {
      actions,
      position: 'bottom-left',
      size: 'medium'
    };
  }

  visualizarAvaliacao(id: number): void {
    this.loading = true;
    this.avaliacoesService.buscarAvaliacaoPorId(id).pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao buscar avaliação:', err);
        alert('Erro ao carregar detalhes da avaliação.');
        return of(null);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(avaliacao => {
      if (avaliacao) {
        console.log('Avaliação encontrada:', avaliacao);
        alert(`Visualizando avaliação do projeto: ${avaliacao.projeto.titulo}`);
      }
    });
  }

  editarAvaliacao(id: number): void {
    console.log('Editar avaliação:', id);
    alert('Funcionalidade de edição será implementada em breve!');
  }

  verProjeto(projetoId: number): void {
    console.log('Ver projeto:', projetoId);
    alert('Navegação para o projeto será implementada em breve!');
  }

  excluirAvaliacao(id: number): void {
    if (!confirm('Tem certeza que deseja excluir esta avaliação? Esta ação não pode ser desfeita.')) {
      return;
    }

    this.loading = true;
    this.avaliacoesService.excluirAvaliacao(id).pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao excluir avaliação:', err);
        alert('Erro ao excluir avaliação. Tente novamente.');
        return of(null);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(() => {
      alert('Avaliação excluída com sucesso!');
      this.loadAvaliacoes();
    });
  }

  getStatusBadgeClass(status: string): string {
    const statusLower = status.toLowerCase();
    switch (statusLower) {
      case 'pendente':
        return 'badge bg-warning text-dark';
      case 'aprovado':
        return 'badge bg-success text-white';
      case 'rejeitado':
        return 'badge bg-danger text-white';
      default:
        return 'badge bg-secondary text-white';
    }
  }

  formatDate(dateString: string): string {
    try {
      const date = new Date(dateString);
      return new Intl.DateTimeFormat('pt-BR').format(date);
    } catch (error) {
      console.error('Erro ao formatar data:', error);
      return dateString;
    }
  }

  formatNota(nota: number): string {
    return nota.toFixed(1);
  }

  clearFilters(): void {
    this.searchTerm = '';
    this.statusFilter = 'TODOS';
    this.applyFilters();
  }

  recarregarDados(): void {
    this.loadAvaliacoes();
  }

  // Métodos para navegação futura
  filtrarPorProjeto(projetoId: number): void {
    this.loading = true;
    this.avaliacoesService.listarAvaliacoesPorProjeto(projetoId).pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao filtrar por projeto:', err);
        return of([]);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(avaliacoes => {
      this.avaliacoes = avaliacoes;
      this.applyFilters();
    });
  }

  filtrarPorAvaliador(avaliadorId: number): void {
    this.loading = true;
    this.avaliacoesService.listarAvaliacoesPorAvaliador(avaliadorId).pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao filtrar por avaliador:', err);
        return of([]);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(avaliacoes => {
      this.avaliacoes = avaliacoes;
      this.applyFilters();
    });
  }
}
