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
  premios: PremioModel[] = [];

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

  toggleModalNovoPremio() {
    this.exibirModalNovoPremio = !this.exibirModalNovoPremio;
  }

  constructor(private premioService: PremioService) {
    this.findAllPremios();
  }
}
