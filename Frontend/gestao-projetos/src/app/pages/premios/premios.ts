import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-premios',
  imports: [CommonModule],
  templateUrl: './premios.html',
  styleUrl: './premios.css'
})

export class Premios {
  premios = [
    {
      nome: 'Prêmio Inovação Tecnológica',
      descricao: 'Reconhecimento para projetos inovadores na área de tecnologia',
      ano: 2023,
      cronograma: 'Submissão de Projetos (01/05/2023 - 30/06/2023)'
    },
    {
      nome: 'Prêmio Sustentabilidade',
      descricao: 'Reconhecimento para projetos com foco em sustentabilidade e meio ambiente',
      ano: 2023,
      cronograma: 'Submissão de Projetos (01/05/2023 - 30/06/2023)'
    },
    {
      nome: 'Prêmio Impacto Social',
      descricao: 'Reconhecimento para projetos com impacto social significativo',
      ano: 2023,
      cronograma: 'Submissão de Projetos (01/05/2023 - 30/06/2023)'
    }
  ];
}
