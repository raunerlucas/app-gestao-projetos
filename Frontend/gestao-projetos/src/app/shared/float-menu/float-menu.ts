import {Component, Input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FloatMenuAction, FloatMenuConfig} from '../../models/FloatMenuAction.model';

@Component({
  selector: 'app-float-menu',
  imports: [CommonModule],
  templateUrl: './float-menu.html',
  styleUrl: './float-menu.css'
})
export class FloatMenu implements OnInit {
  @Input() config: FloatMenuConfig = { actions: [] };

  isOpen = false;

  ngOnInit(): void {
    // Configuração padrão se não fornecida
    if (!this.config.position) {
      this.config.position = 'bottom-right';
    }
    if (!this.config.size) {
      this.config.size = 'medium';
    }
  }

  toggleMenu(): void {
    this.isOpen = !this.isOpen;
  }

  executeAction(action: FloatMenuAction): void {
    if (!action.disabled && action.action) {
      action.action();
      this.isOpen = false; // Fecha o menu após executar ação
    }
  }

  getVisibleActions(): FloatMenuAction[] {
    return this.config.actions.filter(action => action.visible !== false);
  }

  getMenuClasses(): string {
    const position = this.config.position || 'bottom-right';
    const size = this.config.size || 'medium';
    return `float-menu-container ${position} ${size}`;
  }
}
