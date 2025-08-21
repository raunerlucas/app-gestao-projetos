import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';
import {ProjetoParaAvaliacao, StatusAvaliacao} from '../../../models/Avaliacao.model';

@Component({
  selector: 'app-avaliacoes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './avaliacoes.html',
  styleUrl: './avaliacoes.css'
})
export class Avaliacoes implements OnInit {
  projetos: ProjetoParaAvaliacao[] = [];
  filteredProjetos: ProjetoParaAvaliacao[] = [];
  searchTerm: string = '';

  // Expor enum para o template
  readonly StatusAvaliacao = StatusAvaliacao;

  ngOnInit(): void {
    this.loadProjetos();
  }

  private loadProjetos(): void {
    // Dados mockados para demonstração
    this.projetos = [
      {
        id: 1,
        titulo: 'Plataforma de Ensino',
        areaTematica: 'Educação',
        autores: ['Ana Costa'],
        prazo: new Date('2023-06-15'),
        status: StatusAvaliacao.PENDENTE
      },
      {
        id: 2,
        titulo: 'Sistema de Controle',
        areaTematica: 'Engenharia',
        autores: ['João Silva', 'Pedro Santos'],
        prazo: new Date('2023-06-20'),
        status: StatusAvaliacao.PENDENTE
      },
      {
        id: 3,
        titulo: 'Sistema de Gestão Ambiental',
        areaTematica: 'Meio Ambiente',
        autores: ['João Silva'],
        prazo: new Date('2023-06-10'),
        status: StatusAvaliacao.AVALIADO
      },
      {
        id: 4,
        titulo: 'Aplicativo de Monitoramento',
        areaTematica: 'Tecnologia',
        autores: ['Pedro Santos', 'Maria Oliveira'],
        prazo: new Date('2023-06-05'),
        status: StatusAvaliacao.AVALIADO
      }
    ];

    this.filteredProjetos = [...this.projetos];
  }

  onSearch(): void {
    if (!this.searchTerm.trim()) {
      this.filteredProjetos = [...this.projetos];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.filteredProjetos = this.projetos.filter(projeto =>
      projeto.titulo.toLowerCase().includes(term) ||
      projeto.areaTematica.toLowerCase().includes(term) ||
      projeto.autores.some(autor => autor.toLowerCase().includes(term))
    );
  }

  getFloatMenuConfig(projeto: ProjetoParaAvaliacao): FloatMenuConfig {
    if (projeto.status === StatusAvaliacao.PENDENTE) {
      return {
        actions: [
          {
            label: 'Avaliar',
            icon: 'rate_review',
            color: 'primary',
            action: () => this.avaliarProjeto(projeto.id)
          }
        ],
        position: 'bottom-left',
        size: 'medium'
      };
    } else {
      return {
        actions: [
          {
            label: 'Ver Avaliação',
            icon: 'visibility',
            color: 'secondary',
            action: () => this.verAvaliacao(projeto.id)
          }
        ],
        position: 'bottom-left',
        size: 'medium'
      };
    }
  }

  avaliarProjeto(id: number): void {
    console.log('Avaliar projeto:', id);
    // TODO: Implementar modal de avaliação
    alert('Modal de avaliação será implementado em breve!');
  }

  verAvaliacao(id: number): void {
    console.log('Ver avaliação do projeto:', id);
    // TODO: Implementar visualização da avaliação
    alert('Visualização da avaliação será implementada em breve!');
  }

  getStatusBadgeClass(status: StatusAvaliacao): string {
    return status === StatusAvaliacao.PENDENTE
      ? 'badge bg-warning text-dark'
      : 'badge bg-success text-white';
  }

  getStatusLabel(status: StatusAvaliacao): string {
    return status === StatusAvaliacao.PENDENTE ? 'Pendente' : 'Avaliado';
  }

  formatAuthors(autores: string[]): string {
    return autores.join(', ');
  }

  formatDate(date: Date): string {
    return new Intl.DateTimeFormat('pt-BR').format(date);
  }
}
