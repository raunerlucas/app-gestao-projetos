import {Component, Input} from '@angular/core';
import {Logo} from '../logo/logo';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [
    Logo,
    RouterLink
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  @Input() btnLogin: boolean;
  @Input() btnRegister: boolean;
  @Input() showLogo: boolean;
  @Input() userMenu: boolean;

  constructor() {
    this.btnLogin = true
    this.btnRegister = true
    this.showLogo = true;
    this.userMenu = false;
  }

  toggleTheme() {
    // implementar troca de tema claro/escuro
  };
}
