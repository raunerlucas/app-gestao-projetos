import {Component} from '@angular/core';
import {Logo} from '../logo/logo';

@Component({
  selector: 'app-navbar',
  imports: [
    Logo
  ],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  toggleTheme() {
    // implementar troca de tema claro/escuro
  }

  openLoginPage() {

  }
}
