import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {SearchableOption, SearchableSelect} from '../../../../shared/searchable-select/searchable-select';
import {CronogramaSelectService} from '../../../../core/services/cronograma/cronograma-select-service';

@Component({
  selector: 'app-forms-premio',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, SearchableSelect],
  templateUrl: './forms-premio.html',
  styleUrl: './forms-premio.css'
})
export class FormsPremio implements OnInit {
  @Output() fecharModal = new EventEmitter<void>();
  @Output() premioSalvo = new EventEmitter<any>();

  premioForm: FormGroup;
  cronogramasOptions: SearchableOption[] = [];
  carregandoCronogramas = false;

  constructor(
    private fb: FormBuilder,
    private cronogramaSelectService: CronogramaSelectService
  ) {
    this.premioForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(2)]],
      descricao: ['', [Validators.required]],
      ano: ['', [Validators.required, Validators.pattern(/^\d{4}$/)]],
      cronogramaId: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.carregarCronogramas();
  }

  carregarCronogramas(termo: string = ''): void {
    this.carregandoCronogramas = true;

    this.cronogramaSelectService.buscarCronogramas(termo).subscribe({
      next: (cronogramas) => {
        this.cronogramasOptions = cronogramas;
        this.carregandoCronogramas = false;
      },
      error: (error) => {
        console.error('Erro ao carregar cronogramas:', error);
        this.carregandoCronogramas = false;
      }
    });
  }

  onCronogramaSearch(termo: string): void {
    this.carregarCronogramas(termo);
  }

  onCronogramaChange(cronograma: SearchableOption | null): void {
    if (cronograma) {
      console.log('Cronograma selecionado:', cronograma);
    }
  }

  salvar(): void {
    if (this.premioForm.valid) {
      const premio = this.premioForm.value;
      this.premioSalvo.emit(premio);
      this.cancelar();
    } else {
      this.marcarCamposComoTocados();
    }
  }

  cancelar(): void {
    this.premioForm.reset();
    this.fecharModal.emit();
  }

  // Métodos auxiliares para validação
  campoInvalido(campo: string): boolean {
    const control = this.premioForm.get(campo);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  obterMensagemErro(campo: string): string {
    const control = this.premioForm.get(campo);
    if (control?.errors) {
      if (control.errors['required']) return `${campo} é obrigatório`;
      if (control.errors['minlength']) return `${campo} deve ter pelo menos ${control.errors['minlength'].requiredLength} caracteres`;
      if (control.errors['pattern']) return `${campo} deve ter formato válido`;
    }
    return '';
  }

  private marcarCamposComoTocados(): void {
    Object.keys(this.premioForm.controls).forEach(key => {
      this.premioForm.get(key)?.markAsTouched();
    });
  }
}
