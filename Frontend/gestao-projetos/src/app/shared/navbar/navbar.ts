import {Component} from '@angular/core';
import {Logo} from '../logo/logo';
import {NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [
    Logo,
    NgIf,
    RouterLink
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  btnLogin: Boolean = true;
  btnRegister: Boolean = true;

  toggleTheme() {
    // implementar troca de tema claro/escuro
  }
}
