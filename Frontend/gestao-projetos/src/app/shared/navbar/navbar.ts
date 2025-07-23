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
  @Input() btnLogin: Boolean;
  @Input() btnRegister: Boolean;

  constructor() {
    this.btnLogin = true
    this.btnRegister = true
  }

  toggleTheme() {
    // implementar troca de tema claro/escuro
  };
}
