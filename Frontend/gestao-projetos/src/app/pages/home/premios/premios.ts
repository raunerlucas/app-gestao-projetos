import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-premios',
  imports: [CommonModule],
  templateUrl: './premios.html',
  styleUrl: './premios.css'
})

export class Premios {
  exibirModalNovoPremio = false;
  premios: any[] = [ // Dados de exemplo
    { nome: 'Prêmio Inovação', descricao: 'Reconhecimento de projetos inovadores.', ano: '2024', cronograma: 'Q3' },
    { nome: 'Melhor Design', descricao: 'Prêmio para o melhor design de interface.', ano: '2024', cronograma: 'Q4' }
  ];

  toggleModalNovoPremio() {
    this.exibirModalNovoPremio = !this.exibirModalNovoPremio;
  }
}
