import {Component} from '@angular/core';
import {NgClass} from '@angular/common';
import {RouterLink} from '@angular/router';
import {Logo} from '../../shared/logo/logo';
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
    Logo,
    Footer,
    Navbar
  ],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {

  cards: Card[] = [
    {
      title: 'Submissão de Projetos',
      subtitle: 'Submeta seus projetos de forma simples e organizada.',
      icon: 'bi-file-earmark-text',
      link: '/projetos/submissao',
    },
    {
      title: 'Avaliação',
      subtitle: 'Processo de avaliação transparente e eficiente.',
      icon: 'bi-check-circle',
      link: '/avaliacoes',
    },
    {
      title: 'Gestão de Pessoas',
      subtitle: 'Gerencie autores e avaliadores em um só lugar.',
      icon: 'bi-people',
      link: '/pessoas',
    },
    {
      title: 'Premiação',
      subtitle: 'Reconheça os melhores projetos com prêmios personalizados.',
      icon: 'bi-award',
      link: '/premios',
    },
  ];

}

