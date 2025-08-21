import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {CpfMaskPipe} from '../../../shared/pipes/cpf-mask.pipe';
import {PhoneMaskPipe} from '../../../shared/pipes/phone-mask.pipe';
import {AutorService} from '../../../core/services/autor/autor-service';
import {AvaliadorService} from '../../../core/services/avaliador/avaliador-service';
import {AutorModel} from '../../../models/Autor.model';
import {AvaliadorModel} from '../../../models/Avaliador.model';
import {Pessoa, TipoPessoa} from '../../../models/Pessoa.model';
import {combineLatest, of} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {FloatMenu} from '../../../shared/float-menu/float-menu';
import {FloatMenuConfig} from '../../../models/FloatMenuAction.model';


@Component({
  selector: 'app-pessoas',
  imports: [CommonModule, FormsModule, CpfMaskPipe, PhoneMaskPipe, FloatMenu],
  templateUrl: './pessoas.html',
  styleUrl: './pessoas.css'
})
export class Pessoas implements OnInit {
  // Propriedades do componente
  pessoas: Pessoa[] = [];
  filteredPessoas: Pessoa[] = [];
  searchTerm: string = '';
  showModalNovaPessoa: boolean = false;

  constructor(
    private autorService: AutorService,
    private avaliadorService: AvaliadorService
  ) {}

  ngOnInit(): void {
    this.loadPessoas();
  }

  /**
   * Carrega dados mock de pessoas para demonstração
   */
  private loadPessoas(): void {
    combineLatest([
      this.autorService.listarAutores().pipe(catchError(() => of([] as AutorModel[]))),
      this.avaliadorService.listarAvaliadores().pipe(catchError(() => of([] as AvaliadorModel[])))
    ]).subscribe({
      next: ([autores, avaliadores]) => {
        this.pessoas = [
          ...autores.map(autor => ({
            id: autor.id,
            nome: autor.nome,
            cpf: autor.cpf,
            telefone: autor.telefone,
            email: autor.email,
            tipo: TipoPessoa.AUTOR,
            projetosIds: autor.projetosIds
          })),
          ...avaliadores.map(avaliador => ({
            id: avaliador.id,
            nome: avaliador.nome,
            cpf: avaliador.cpf,
            telefone: avaliador.telefone,
            email: avaliador.email,
            tipo: TipoPessoa.AVALIADOR
          }))
        ];
        this.filteredPessoas = [...this.pessoas];
      },
      error: (err) => {
        this.pessoas = [];
        this.filteredPessoas = [];
        console.error('Erro ao buscar pessoas:', err);
      }
    });
  }

  /**
   * Realiza a busca por nome, email ou CPF
   */
  onSearch(): void {
    if (!this.searchTerm.trim()) {
      this.filteredPessoas = [...this.pessoas];
      return;
    }

    const term = this.searchTerm.toLowerCase().trim();
    this.filteredPessoas = this.pessoas.filter(pessoa =>
      pessoa.nome.toLowerCase().includes(term) ||
      pessoa.email.toLowerCase().includes(term) ||
      pessoa.cpf.replace(/\D/g, '').includes(term.replace(/\D/g, ''))
    );
  }

  /**
   * Retorna o rótulo do tipo de pessoa
   */
  getTipoLabel(tipo: TipoPessoa): string {
    const labels: Record<TipoPessoa, string> = {
      [TipoPessoa.AUTOR]: 'Autor',
      [TipoPessoa.AVALIADOR]: 'Avaliador',
    };
    return labels[tipo] || 'Desconhecido';
  }

  /**
   * Retorna a classe CSS para o badge do tipo
   */
  getTipoBadgeClass(tipo: TipoPessoa): string {
    const classes: Record<TipoPessoa, string> = {
      [TipoPessoa.AUTOR]: 'bg-primary',
      [TipoPessoa.AVALIADOR]: 'bg-success',
    };
    return classes[tipo] || 'bg-secondary';
  }

  /**
   * Abre/fecha o modal de nova pessoa
   */
  toggleModalNovaPessoa(): void {
    this.showModalNovaPessoa = !this.showModalNovaPessoa;
  }

  /**
   * Navega para edição da pessoa (placeholder para funcionalidade futura)
   */
  editarPessoa(id: number): void {
    // Implementação real pode abrir modal ou navegar para edição
    alert('Editar pessoa: ' + id);
  }

  getFloatMenuConfig(pessoa: Pessoa): FloatMenuConfig {
    return {
      actions: [
        {
          label: 'Editar',
          icon: 'edit',
          color: 'primary',
          action: () => this.editarPessoa(pessoa.id)
        },
        {
          label: 'Excluir',
          icon: 'delete',
          color: 'danger',
          action: () => this.excluirPessoa(pessoa.id)
        }
      ],
      position: 'bottom-left',
      size: 'medium'
    };
  }

  excluirPessoa(id: number): void {
    // TODO: Implementar exclusão real via service
    this.pessoas = this.pessoas.filter(p => p.id !== id);
    this.filteredPessoas = this.filteredPessoas.filter(p => p.id !== id);
    alert('Pessoa excluída!');
  }
}
