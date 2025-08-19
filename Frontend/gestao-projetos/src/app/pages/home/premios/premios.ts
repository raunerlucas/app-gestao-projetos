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

  constructor(private premioService: PremioService) {
  }

  ngOnInit(): void {
    this.findAllPremios();
  }

  private findAllPremios() {
    return this.premioService.listarPremios().subscribe(
      (data: PremioModel[]) => {
        this.premios = data;
        },
      (error) => {
        console.error('Erro ao buscar prêmios:', error);
      }
      );
  }

  // Criar configuração específica para cada prêmio
  getMenuConfigForPremio(premio: PremioModel): FloatMenuConfig {
    return {
      position: 'bottom-left',
      size: 'small',
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

  toggleModalNovoPremio() {
    this.exibirModalNovoPremio = !this.exibirModalNovoPremio;
  }

  // Método para recarregar a lista após operações CRUD
  recarregarPremios(): void {
    this.findAllPremios();
  }

  // Método para visualizar prêmio específico
  visualizarPremio(premio: PremioModel): void {
    console.log('Visualizando prêmio:', premio);
    // Implementar lógica de visualização aqui
    // Pode abrir um modal com detalhes ou navegar para página de detalhes
  }

  // Método para editar prêmio específico
  editarPremio(premio: PremioModel): void {
    console.log('Editando prêmio:', premio);
    // Implementar lógica de edição aqui
    // Pode abrir modal de edição ou navegar para página de edição
  }

  // Método para deletar prêmio específico
  deletarPremio(id: number): void {
    if (confirm('Tem certeza que deseja deletar este prêmio?')) {
      this.premioService.deletarPremio(id).subscribe(
        () => {
          this.recarregarPremios();
          console.log('Prêmio deletado com sucesso');
        },
        (error) => {
          console.error('Erro ao deletar prêmio:', error);
        }
      );
    }
  }
}
