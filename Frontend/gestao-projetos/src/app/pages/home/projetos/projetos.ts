import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {of, Subject} from 'rxjs';
import {catchError, finalize, takeUntil} from 'rxjs/operators';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';
import {Projeto} from '../../../models/Projeto.model';
import {ProjetosService} from '../../../core/services/projetos/projetosService';

@Component({
  selector: 'app-projetos',
  standalone: true,
  imports: [CommonModule, FormsModule, FloatMenu],
  templateUrl: './projetos.html',
  styleUrl: './projetos.css'
})
export class Projetos implements OnInit, OnDestroy {
  projetos: Projeto[] = [];
  filteredProjetos: Projeto[] = [];
  searchTerm: string = '';
  statusFilter: string = 'TODOS';
  showModalNovoProjeto: boolean = false;
  loading: boolean = false;
  error: string | null = null;

  private destroy$ = new Subject<void>();

  // Opções do filtro de status
  statusOptions = [
    { value: 'TODOS', label: 'Todos' },
    { value: 'SEM_AVALIACOES', label: 'Sem avaliações' },
    { value: 'COM_AVALIACOES', label: 'Com avaliações' },
    { value: 'VENCEDORES', label: 'Vencedores' }
  ];

  constructor(private projetosService: ProjetosService) {}

  ngOnInit(): void {
    this.loadProjetos();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadProjetos(): void {
    this.loading = true;
    this.error = null;

    // Determinar qual endpoint usar baseado no filtro
    let serviceCall;
    switch (this.statusFilter) {
      case 'SEM_AVALIACOES':
        serviceCall = this.projetosService.listarProjetosSemAvaliacoes();
        break;
      case 'COM_AVALIACOES':
        serviceCall = this.projetosService.listarProjetosComAvaliacoes();
        break;
      case 'VENCEDORES':
        serviceCall = this.projetosService.listarProjetosVencedores();
        break;
      default:
        serviceCall = this.projetosService.listarProjetos();
        break;
    }

    serviceCall.pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao carregar projetos:', err);
        this.error = 'Erro ao carregar projetos. Tente novamente.';
        return of([]);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(projetos => {
      this.projetos = projetos;
      this.applySearchFilter();
    });
  }

  onSearch(): void {
    this.applySearchFilter();
  }

  onStatusFilterChange(): void {
    this.loadProjetos();
  }

  private applySearchFilter(): void {
    if (!this.searchTerm.trim()) {
      this.filteredProjetos = [...this.projetos];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.filteredProjetos = this.projetos.filter(projeto =>
      projeto.titulo.toLowerCase().includes(term) ||
      projeto.areaTematica.toLowerCase().includes(term) ||
      projeto.autores.some(autor => autor.nome.toLowerCase().includes(term))
    );
  }

  getFloatMenuConfig(projeto: Projeto): FloatMenuConfig {
    const actions = [
      {
        label: 'Visualizar',
        icon: 'visibility',
        color: 'primary',
        action: () => this.visualizarProjeto(projeto.id)
      },
      {
        label: 'Editar',
        icon: 'edit',
        color: 'secondary',
        action: () => this.editarProjeto(projeto.id)
      }
    ];

    // Adicionar ação "Ver Avaliações" se o projeto tiver avaliações
    if (projeto.avaliacoes && projeto.avaliacoes.length > 0) {
      actions.push({
        label: 'Ver Avaliações',
        icon: 'rate_review',
        color: 'success' as const,
        action: () => this.verAvaliacoes(projeto.id)
      });
    }

    actions.push({
      label: 'Excluir',
      icon: 'delete',
      color: 'danger' as const,
      action: () => this.excluirProjeto(projeto.id)
    });

    return {
      actions,
      position: 'bottom-left',
      size: 'medium'
    } as FloatMenuConfig;
  }

  toggleModalNovoProjeto(): void {
    this.showModalNovoProjeto = !this.showModalNovoProjeto;
  }

  visualizarProjeto(id: number): void {
    this.loading = true;
    this.projetosService.buscarProjetoPorId(id).pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao buscar projeto:', err);
        alert('Erro ao carregar detalhes do projeto.');
        return of(null);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(projeto => {
      if (projeto) {
        console.log('Projeto encontrado:', projeto);
        // TODO: Implementar modal de visualização ou navegação
        alert(`Visualizando projeto: ${projeto.titulo}`);
      }
    });
  }

  editarProjeto(id: number): void {
    console.log('Editar projeto:', id);
    // TODO: Implementar navegação para edição ou modal
    alert('Funcionalidade de edição será implementada em breve!');
  }

  verAvaliacoes(id: number): void {
    console.log('Ver avaliações do projeto:', id);
    // TODO: Implementar modal ou navegação para visualizar avaliações
    alert('Visualização das avaliações será implementada em breve!');
  }

  excluirProjeto(id: number): void {
    if (!confirm('Tem certeza que deseja excluir este projeto? Esta ação não pode ser desfeita.')) {
      return;
    }

    this.loading = true;
    this.projetosService.excluirProjeto(id).pipe(
      takeUntil(this.destroy$),
      catchError((err) => {
        console.error('Erro ao excluir projeto:', err);
        alert('Erro ao excluir projeto. Tente novamente.');
        return of(null);
      }),
      finalize(() => {
        this.loading = false;
      })
    ).subscribe(() => {
      alert('Projeto excluído com sucesso!');
      this.loadProjetos(); // Recarregar a lista
    });
  }

  getStatusDisplay(projeto: Projeto): string {
    if (projeto.avaliacoes && projeto.avaliacoes.length > 0) {
      // Calcular média das notas
      const mediaNotas = projeto.avaliacoes.reduce((sum, av) => sum + av.nota, 0) / projeto.avaliacoes.length;
      return `Avaliado (${mediaNotas.toFixed(1)})`;
    } else {
      return 'Pendente';
    }
  }

  getStatusBadgeClass(projeto: Projeto): string {
    if (projeto.avaliacoes && projeto.avaliacoes.length > 0) {
      return 'badge bg-success text-white';
    } else {
      return 'badge bg-warning text-dark';
    }
  }

  formatAuthors(autores: any[]): string {
    if (Array.isArray(autores) && autores.length > 0) {
      return autores.map(autor => autor.nome || autor).join(', ');
    }
    return 'Sem autores';
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

  // Método para limpar filtros
  clearFilters(): void {
    this.searchTerm = '';
    this.statusFilter = 'TODOS';
    this.loadProjetos();
  }

  // Método para recarregar dados
  recarregarDados(): void {
    this.loadProjetos();
  }
}
