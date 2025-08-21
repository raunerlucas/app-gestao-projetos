import {Component, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {CpfMaskPipe} from '../../../shared/pipes/cpf-mask.pipe';
import {PhoneMaskPipe} from '../../../shared/pipes/phone-mask.pipe';

// Interface para representar uma pessoa
interface Pessoa {
  id?: number;
  nome: string;
  cpf: string;
  email: string;
  telefone: string;
  tipo: TipoPessoa;
}

// Enum para tipos de pessoa
enum TipoPessoa {
  AUTOR = 'AUTOR',
  AVALIADOR = 'AVALIADOR',
  COORDENADOR = 'COORDENADOR',
  ADMINISTRADOR = 'ADMINISTRADOR'
}

@Component({
  selector: 'app-pessoas',
  imports: [CommonModule, FormsModule, CpfMaskPipe, PhoneMaskPipe],
  templateUrl: './pessoas.html',
  styleUrl: './pessoas.css'
})
export class Pessoas implements OnInit {
  // Propriedades do componente
  pessoas: Pessoa[] = [];
  filteredPessoas: Pessoa[] = [];
  searchTerm: string = '';
  showModalNovaPessoa: boolean = false;

  constructor() {}

  ngOnInit(): void {
    this.loadPessoas();
  }

  /**
   * Carrega dados mock de pessoas para demonstração
   */
  private loadPessoas(): void {
    // Dados mock para demonstração
    this.pessoas = [
      {
        id: 1,
        nome: 'João Silva',
        cpf: '123.456.789-00',
        email: 'joao.silva@example.com',
        telefone: '(11) 99999-9999',
        tipo: TipoPessoa.AUTOR
      },
      {
        id: 2,
        nome: 'Maria Santos',
        cpf: '987.654.321-00',
        email: 'maria.santos@example.com',
        telefone: '(11) 88888-8888',
        tipo: TipoPessoa.AVALIADOR
      },
      {
        id: 3,
        nome: 'Pedro Oliveira',
        cpf: '456.789.123-00',
        email: 'pedro.oliveira@example.com',
        telefone: '(11) 77777-7777',
        tipo: TipoPessoa.COORDENADOR
      },
      {
        id: 4,
        nome: 'Ana Costa',
        cpf: '789.123.456-00',
        email: 'ana.costa@example.com',
        telefone: '(11) 66666-6666',
        tipo: TipoPessoa.ADMINISTRADOR
      },
      {
        id: 5,
        nome: 'Carlos Ferreira',
        cpf: '321.654.987-00',
        email: 'carlos.ferreira@example.com',
        telefone: '(11) 55555-5555',
        tipo: TipoPessoa.AUTOR
      }
    ];

    this.filteredPessoas = [...this.pessoas];
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
      [TipoPessoa.COORDENADOR]: 'Coordenador',
      [TipoPessoa.ADMINISTRADOR]: 'Administrador'
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
      [TipoPessoa.COORDENADOR]: 'bg-info',
      [TipoPessoa.ADMINISTRADOR]: 'bg-warning'
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
    console.log('Editar pessoa com ID:', id);
    // TODO: Implementar navegação para edição
    alert('Funcionalidade de edição será implementada em breve!');
  }
}
