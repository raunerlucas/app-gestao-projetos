import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';
import {Projeto, StatusProjeto} from '../../../models/Projeto.model';

@Component({
  selector: 'app-projetos',
  standalone: true,
  imports: [CommonModule, FormsModule, FloatMenu],
  templateUrl: './projetos.html',
  styleUrl: './projetos.css'
})
export class Projetos implements OnInit {
  projetos: Projeto[] = [];
  filteredProjetos: Projeto[] = [];
  searchTerm: string = '';
  statusFilter: string = 'TODOS';
  showModalNovoProjeto: boolean = false;

  // Expor enum para o template
  readonly StatusProjeto = StatusProjeto;

  // Opções do filtro de status
  statusOptions = [
    { value: 'TODOS', label: 'Todos' },
    { value: 'PENDENTE', label: 'Pendente' },
    { value: 'EM_AVALIACAO', label: 'Em avaliação' },
    { value: 'AVALIADO', label: 'Avaliado' }
  ];

  ngOnInit(): void {
    this.loadProjetos();
  }

  private loadProjetos(): void {
    // Dados mockados conforme especificação
    this.projetos = [
      {
        id: 1,
        titulo: 'Sistema de Gestão Ambiental',
        areaTematica: 'Meio Ambiente',
        dataEnvio: new Date('2023-05-10'),
        autores: ['João Silva'],
        status: StatusProjeto.AVALIADO,
        nota: 8.5
      },
      {
        id: 2,
        titulo: 'Aplicativo de Monitoramento',
        areaTematica: 'Tecnologia',
        dataEnvio: new Date('2023-04-22'),
        autores: ['Pedro Santos', 'Maria Oliveira'],
        status: StatusProjeto.AVALIADO,
        nota: 9.2
      },
      {
        id: 3,
        titulo: 'Plataforma de Ensino',
        areaTematica: 'Educação',
        dataEnvio: new Date('2023-06-05'),
        autores: ['Ana Costa'],
        status: StatusProjeto.PENDENTE
      },
      {
        id: 4,
        titulo: 'Sistema de Controle',
        areaTematica: 'Engenharia',
        dataEnvio: new Date('2023-06-01'),
        autores: ['João Silva', 'Pedro Santos'],
        status: StatusProjeto.EM_AVALIACAO
      },
      {
        id: 5,
        titulo: 'Aplicativo de Saúde',
        areaTematica: 'Saúde',
        dataEnvio: new Date('2023-05-15'),
        autores: ['Maria Oliveira'],
        status: StatusProjeto.AVALIADO,
        nota: 7.8
      }
    ];

    this.applyFilters();
  }

  onSearch(): void {
    this.applyFilters();
  }

  onStatusFilterChange(): void {
    this.applyFilters();
  }

  protected applyFilters(): void {
    let filtered = [...this.projetos];

    // Filtrar por termo de busca
    if (this.searchTerm.trim()) {
      const term = this.searchTerm.toLowerCase().trim();
      filtered = filtered.filter(projeto =>
        projeto.titulo.toLowerCase().includes(term) ||
        projeto.areaTematica.toLowerCase().includes(term) ||
        projeto.autores.some(autor => autor.toLowerCase().includes(term))
      );
    }

    // Filtrar por status
    if (this.statusFilter !== 'TODOS') {
      filtered = filtered.filter(projeto => projeto.status === this.statusFilter);
    }

    this.filteredProjetos = filtered;
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

    if (projeto.status === StatusProjeto.AVALIADO) {
      actions.push({
        label: 'Ver Avaliação',
        icon: 'rate_review',
        color: 'success',
        action: () => this.verAvaliacao(projeto.id)
      });
    }

    actions.push({
      label: 'Excluir',
      icon: 'delete',
      color: 'danger',
      action: () => this.excluirProjeto(projeto.id)
    });

    return {
      actions: actions,
      position: 'bottom-left',
      size: 'medium'
    } as FloatMenuConfig;
  }

  toggleModalNovoProjeto(): void {
    this.showModalNovoProjeto = !this.showModalNovoProjeto;
  }

  visualizarProjeto(id: number): void {
    console.log('Visualizar projeto:', id);
    alert('Visualização do projeto será implementada em breve!');
  }

  editarProjeto(id: number): void {
    console.log('Editar projeto:', id);
    alert('Edição do projeto será implementada em breve!');
  }

  verAvaliacao(id: number): void {
    console.log('Ver avaliação do projeto:', id);
    alert('Visualização da avaliação será implementada em breve!');
  }

  excluirProjeto(id: number): void {
    if (confirm('Tem certeza que deseja excluir este projeto?')) {
      this.projetos = this.projetos.filter(p => p.id !== id);
      this.applyFilters();
      alert('Projeto excluído com sucesso!');
    }
  }

  getStatusDisplay(projeto: Projeto): string {
    if (projeto.status === StatusProjeto.AVALIADO && projeto.nota) {
      return `Avaliado (${projeto.nota})`;
    }
    return this.getStatusLabel(projeto.status);
  }

  getStatusLabel(status: StatusProjeto): string {
    const labels = {
      [StatusProjeto.PENDENTE]: 'Pendente',
      [StatusProjeto.EM_AVALIACAO]: 'Em avaliação',
      [StatusProjeto.AVALIADO]: 'Avaliado'
    };
    return labels[status];
  }

  getStatusBadgeClass(status: StatusProjeto): string {
    const classes = {
      [StatusProjeto.PENDENTE]: 'badge bg-warning text-dark',
      [StatusProjeto.EM_AVALIACAO]: 'badge bg-info text-white',
      [StatusProjeto.AVALIADO]: 'badge bg-success text-white'
    };
    return classes[status];
  }

  formatAuthors(autores: string[]): string {
    return autores.join(', ');
  }

  formatDate(date: Date): string {
    return new Intl.DateTimeFormat('pt-BR').format(date);
  }
}
