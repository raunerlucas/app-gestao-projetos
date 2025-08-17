import {Component, EventEmitter, Output} from '@angular/core';


@Component({
  selector: 'app-forms-premio',
  standalone: true,
  imports: [],
  templateUrl: './forms-premio.html',
  styleUrl: './forms-premio.css'
})
export class FormsPremio {
  @Output() fecharModal = new EventEmitter<void>();

  cancelar() {
    this.fecharModal.emit();
  }
}
