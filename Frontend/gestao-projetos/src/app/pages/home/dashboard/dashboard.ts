import {Component, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {combineLatest, of, Subject} from 'rxjs';
import {catchError, finalize, takeUntil} from 'rxjs/operators';

import {ProjetosService} from '../../../core/services/projetos/projetosService';
import {AvaliacoesService} from '../../../core/services/avaliacoes/avaliacoes-service';
import {AutorService} from '../../../core/services/autor/autor-service';
import {AvaliadorService} from '../../../core/services/avaliador/avaliador-service';

import {Projeto} from '../../../models/Projeto.model';
import {Avaliacao} from '../../../models/Avaliacao.model';
import {AutorModel} from '../../../models/Autor.model';
import {AvaliadorModel} from '../../../models/Avaliador.model';

interface DashboardMetrics {
  totalProjetos: number;
  projetosAvaliados: number;
  projetosPendentes: number;
  totalAvaliacoes: number;
  avaliacoesPendentes: number;
  totalPessoas: number;
  autores: number;
  avaliadores: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit, OnDestroy {
  selectedTab: string = 'projetos';
  loading: boolean = false;
  error: string | null = null;

  metrics: DashboardMetrics = {
    totalProjetos: 0,
    projetosAvaliados: 0,
    projetosPendentes: 0,
    totalAvaliacoes: 0,
    avaliacoesPendentes: 0,
    totalPessoas: 0,
    autores: 0,
    avaliadores: 0
  };

  projetosRecentes: Projeto[] = [];
  avaliacoesPendentes: Avaliacao[] = [];
  projetosDestaque: Projeto[] = [];

  private destroy$ = new Subject<void>();

  constructor(
    private projetosService: ProjetosService,
    private avaliacoesService: AvaliacoesService,
    private autorService: AutorService,
    private avaliadorService: AvaliadorService
  ) {}

  ngOnInit(): void {
    this.loadDashboardData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadDashboardData(): void {
    this.loading = true;
    this.error = null;

    combineLatest([
      this.projetosService.listarProjetos().pipe(catchError(() => of([]))),
      this.avaliacoesService.listarAvaliacoes().pipe(catchError(() => of([]))),
      this.autorService.listarAutores().pipe(catchError(() => of([]))),
      this.avaliadorService.listarAvaliadores().pipe(catchError(() => of([])))
    ]).pipe(
      takeUntil(this.destroy$),
      finalize(() => this.loading = false)
    ).subscribe({
      next: ([projetos, avaliacoes, autores, avaliadores]) => {
        this.processMetrics(projetos, avaliacoes, autores, avaliadores);
        this.processProjetosRecentes(projetos);
        this.processAvaliacoesPendentes(avaliacoes);
        this.processProjetosDestaque(projetos);
      },
      error: (err) => {
        console.error('Erro ao carregar dados do dashboard:', err);
        this.error = 'Erro ao carregar dados do dashboard.';
      }
    });
  }

  private processMetrics(
    projetos: Projeto[],
    avaliacoes: Avaliacao[],
    autores: AutorModel[],
    avaliadores: AvaliadorModel[]
  ): void {
    const projetosComAvaliacao = projetos.filter(p => p.avaliacoes && p.avaliacoes.length > 0);
    const projetosSemAvaliacao = projetos.filter(p => !p.avaliacoes || p.avaliacoes.length === 0);
    const avaliacoesPendentes = avaliacoes.filter(a => a.status.description.toLowerCase() === 'pendente');

    this.metrics = {
      totalProjetos: projetos.length,
      projetosAvaliados: projetosComAvaliacao.length,
      projetosPendentes: projetosSemAvaliacao.length,
      totalAvaliacoes: avaliacoes.length,
      avaliacoesPendentes: avaliacoesPendentes.length,
      totalPessoas: autores.length + avaliadores.length,
      autores: autores.length,
      avaliadores: avaliadores.length
    };
  }

  private processProjetosRecentes(projetos: Projeto[]): void {
    this.projetosRecentes = projetos
      .sort((a, b) => new Date(b.dataEnvio).getTime() - new Date(a.dataEnvio).getTime())
      .slice(0, 6);
  }

  private processAvaliacoesPendentes(avaliacoes: Avaliacao[]): void {
    this.avaliacoesPendentes = avaliacoes
      .filter(a => a.status.description.toLowerCase() === 'pendente')
      .slice(0, 5);
  }

  private processProjetosDestaque(projetos: Projeto[]): void {
    this.projetosDestaque = projetos
      .filter(p => p.avaliacoes && p.avaliacoes.length > 0)
      .sort((a, b) => {
        const mediaA = a.avaliacoes!.reduce((sum, av) => sum + av.nota, 0) / a.avaliacoes!.length;
        const mediaB = b.avaliacoes!.reduce((sum, av) => sum + av.nota, 0) / b.avaliacoes!.length;
        return mediaB - mediaA;
      })
      .slice(0, 3);
  }

  setTab(tab: string): void {
    this.selectedTab = tab;
  }

  getProjetoStatus(projeto: Projeto): string {
    if (projeto.avaliacoes && projeto.avaliacoes.length > 0) {
      const media = projeto.avaliacoes.reduce((sum, av) => sum + av.nota, 0) / projeto.avaliacoes.length;
      return `Avaliado (${media.toFixed(1)})`;
    }
    return 'Pendente';
  }

  getProjetoStatusClass(projeto: Projeto): string {
    if (projeto.avaliacoes && projeto.avaliacoes.length > 0) {
      return 'status-avaliado';
    }
    return 'status-pendente';
  }

  formatDate(dateString: string): string {
    try {
      const date = new Date(dateString);
      return new Intl.DateTimeFormat('pt-BR').format(date);
    } catch {
      return dateString;
    }
  }

  formatAuthors(autores: any[]): string {
    if (Array.isArray(autores) && autores.length > 0) {
      return autores.map(autor => autor.nome || autor).join(', ');
    }
    return 'Sem autores';
  }

  recarregarDados(): void {
    this.loadDashboardData();
  }
}
