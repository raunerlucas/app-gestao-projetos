import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsPremio} from './forms-premio/forms-premio';
import {PremioService} from '../../../core/services/premio/premio-service';
import {PremioModel} from '../../../models/Premio.model';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';

@Component({
  selector: 'app-premios',
  imports: [CommonModule, FormsPremio, FloatMenu],
  templateUrl: './premios.html',
  styleUrl: './premios.css'
})

export class Premios implements OnInit {
  exibirModalNovoPremio = false;
  premios: PremioModel[] = [];
  carregando = false;
  erro: string | null = null;

  constructor(private premioService: PremioService) {}

  ngOnInit(): void {
    this.carregarPremios();
  }

  // Carregar prêmios da API
  private carregarPremios(): void {
    this.carregando = true;
    this.erro = null;

    this.premioService.listarPremios().subscribe({
      next: (premios: PremioModel[]) => {
        this.premios = premios;
        console.log('Prêmios carregados:', premios);
      },
      error: (error) => {
        this.erro = 'Erro ao carregar prêmios. Tente novamente.';
        console.error('Erro ao carregar prêmios:', error);
        this.carregando = false;
      },
      complete: () => {
        this.carregando = false;
      }
    });
  }

  // Criar configuração específica para cada prêmio
  getMenuConfigForPremio(premio: PremioModel): FloatMenuConfig {
    return {
      actions: [
        {
          label: 'Visualizar',
          icon: 'visibility',
          color: 'info',
          action: () => this.visualizarPremio(premio)
        },
        {
          label: 'Editar',
          icon: 'edit',
          color: 'primary',
          action: () => this.editarPremio(premio)
        },
        {
          label: 'Deletar',
          icon: 'delete',
          color: 'danger',
          action: () => this.deletarPremio(premio.id!)
        }
      ]
    };
  }

  toggleModalNovoPremio(): void {
    this.exibirModalNovoPremio = !this.exibirModalNovoPremio;
    this.erro = null; // Limpar erro ao abrir/fechar modal
  }

  // Método chamado pelo formulário quando um prêmio é salvo
  onPremioSalvo(premioData: any): void {
    this.carregando = true;

    const novoPremio: PremioModel = {
      id: null,
      nome: premioData.nome,
      descricao: premioData.descricao,
      anoEdicao: parseInt(premioData.ano),
      cronograma: premioData.cronogramaId
    };

    this.premioService.criarPremio(novoPremio).subscribe({
      next: (premioSalvo: PremioModel) => {
        this.premios.push(premioSalvo);
        console.log('Prêmio criado com sucesso:', premioSalvo);
        this.toggleModalNovoPremio();
        // Mostrar mensagem de sucesso se necessário
      },
      error: (error) => {
        console.error('Erro ao criar prêmio:', error);
        this.erro = 'Erro ao salvar prêmio. Tente novamente.';
      },
      complete: () => {
        this.carregando = false;
      }
    });
  }

  // Método para recarregar a lista após operações CRUD
  recarregarPremios(): void {
    this.carregarPremios();
  }

  // Método para visualizar prêmio específico
  visualizarPremio(premio: PremioModel): void {
    console.log('👁️ Visualizar:', premio);
    // Implementar lógica de visualização aqui
  }

  // Método para editar prêmio específico
  editarPremio(premio: PremioModel): void {
    console.log('✏️ Editar:', premio);
    // Implementar lógica de edição aqui
  }

  // Método para deletar prêmio específico
  deletarPremio(id: number): void {
    if (confirm('Tem certeza que deseja deletar este prêmio?')) {
      this.carregando = true;

      this.premioService.deletarPremio(id).subscribe({
        next: () => {
          this.premios = this.premios.filter(p => p.id !== id);
          console.log('🗑️ Prêmio deletado com sucesso');
        },
        error: (error) => {
          console.error('Erro ao deletar prêmio:', error);
          this.erro = 'Erro ao deletar prêmio. Tente novamente.';
        },
        complete: () => {
          this.carregando = false;
        }
      });
    }
  }
}
