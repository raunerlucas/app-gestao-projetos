import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsPremio} from './forms-premio/forms-premio';
import {PremioService} from '../../../core/services/premio/premio-service';
import {PremioModel} from '../../../models/Premio.model';

@Component({
  selector: 'app-premios',
  imports: [CommonModule, FormsPremio],
  templateUrl: './premios.html',
  styleUrl: './premios.css'
})

export class Premios {
  exibirModalNovoPremio = false;
  premios: any[] = [ // Dados de exemplo
    { nome: 'Prêmio Inovação', descricao: 'Reconhecimento de projetos inovadores.', ano: '2024', cronograma: 'Q3' },
    { nome: 'Melhor Design', descricao: 'Prêmio para o melhor design de interface.', ano: '2024', cronograma: 'Q4' }
  ];

  premioServer: PremioModel[] | undefined;

  private findAllPremios() {
    return this.premioService.listarPremios().subscribe(
      (data) => {
        console.log(data)
        },
      (error) => {
        console.error('Erro ao buscar prêmios:', error);
      }
      );
  }

  toggleModalNovoPremio() {
    this.exibirModalNovoPremio = !this.exibirModalNovoPremio;
  }

  constructor(private premioService: PremioService) {
    this.findAllPremios();
  }
}
