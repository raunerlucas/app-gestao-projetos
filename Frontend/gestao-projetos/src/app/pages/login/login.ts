import {Component} from '@angular/core';
import {Navbar} from '../../shared/navbar/navbar';
import {Router, RouterLink} from '@angular/router';
import {FormsModule} from '@angular/forms';
import {Auth} from '../../core/services/auth';

@Component({
  selector: 'app-login',
  imports: [
    Navbar,
    RouterLink,
    FormsModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  email: string | undefined;
  senha: string | undefined;

  constructor(private auth: Auth, private router: Router) {}

  onLogin() {
    if (this.email && this.senha) {
      this.auth.login(this.email, this.senha) ?
        this.router.navigate(['/dashboard']) :
        console.error('Falha no login. Verifique suas credenciais.');
    } else {
      console.error('Por favor, preencha todos os campos.');
    }
  }
}
