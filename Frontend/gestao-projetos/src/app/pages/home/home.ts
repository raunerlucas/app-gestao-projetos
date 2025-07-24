import {Component} from '@angular/core';
import {NgClass} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Footer} from '../../shared/footer/footer';
import {Navbar} from '../../shared/navbar/navbar';

interface Card {
  title: string;
  subtitle: string;
  icon: string;  // Usar classes do Bootstrap Icons
  link: string;
}

@Component({
  selector: 'app-home',
  imports: [
    NgClass,
    RouterLink,
    Footer,
    Navbar
  ],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  link = '/dashboard';

  cards: Card[] = [
    {
      title: 'Submissão de Projetos',
      subtitle: 'Submeta seus projetos de forma simples e organizada.',
      icon: 'bi-file-earmark-text',
      link: this.link,
    },
    {
      title: 'Avaliação',
      subtitle: 'Processo de avaliação transparente e eficiente.',
      icon: 'bi-check-circle',
      link: this.link,
    },
    {
      title: 'Gestão de Pessoas',
      subtitle: 'Gerencie autores e avaliadores em um só lugar.',
      icon: 'bi-people',
      link: this.link,
    },
    {
      title: 'Premiação',
      subtitle: 'Reconheça os melhores projetos com prêmios personalizados.',
      icon: 'bi-award',
      link: this.link,
    },
  ];

}

