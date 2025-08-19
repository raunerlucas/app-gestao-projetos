import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FloatMenuAction, FloatMenuConfig} from '../../models/FloatMenuAction.model';
import {FloatMenuService} from '../../core/services/float-menu/float-menu.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-float-menu',
  imports: [CommonModule],
  templateUrl: './float-menu.html',
  styleUrl: './float-menu.css'
})
export class FloatMenu implements OnInit, OnDestroy {
  @Input() config: FloatMenuConfig = { actions: [] };

  isOpen = false;
  menuId: string;
  private subscription: Subscription = new Subscription();

  constructor(private floatMenuService: FloatMenuService) {
    // Gerar ID único para este menu
    this.menuId = 'menu_' + Math.random().toString(36).substr(2, 9);
  }

  ngOnInit(): void {
    // Configuração padrão se não fornecida
    if (!this.config.position) {
      this.config.position = 'bottom-right';
    }
    if (!this.config.size) {
      this.config.size = 'medium';
    }

    // Inscrever-se para fechar este menu quando outro for aberto
    this.subscription = this.floatMenuService.closeAllMenus$.subscribe(
      (exceptMenuId: string) => {
        if (exceptMenuId !== this.menuId && this.isOpen) {
          this.isOpen = false;
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  toggleMenu(): void {
    if (!this.isOpen) {
      // Fechar todos os outros menus antes de abrir este
      this.floatMenuService.closeAllMenusExcept(this.menuId);
    }
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
