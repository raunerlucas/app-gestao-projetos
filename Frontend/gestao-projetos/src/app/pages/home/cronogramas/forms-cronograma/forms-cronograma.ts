import {Component} from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';

@Component({
  selector: 'app-forms-cronograma',
  imports: [
    FormsModule,
    ReactiveFormsModule
  ],
  templateUrl: './forms-cronograma.html',
  styleUrl: './forms-cronograma.css'
})
export class FormsCronograma {
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      descricao: ['', Validators.required],
      dataInicio: ['', Validators.required],
      dataFim: ['', Validators.required],
      status: ['pendente', Validators.required]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      console.log('📌 Dados enviados:', this.form.value);
      // Aqui você chama o service de API futuramente
    } else {
      this.form.markAllAsTouched();
    }
  }

  onCancelar() {
    this.form.reset({
      status: 'pendente'
    });
  }
}
