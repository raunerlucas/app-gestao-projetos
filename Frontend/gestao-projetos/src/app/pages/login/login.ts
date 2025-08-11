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
  email: string;
  senha: string;

  constructor(private auth: Auth, private router: Router) {
    this.email = '';
    this.senha = '';
  }

  onLogin() {
    if (!this.email || !this.senha) {
      console.error('Por favor, preencha todos os campos.');
      return;
    }
    this.auth.login(this.email, this.senha).subscribe(
      success => {
        if (success) {
          this.router.navigate(['/dashboard']);
        } else {
          this.router.navigate(['/dashboard']); // TODO: Apenas para teste, remover depois
          console.error('Falha no login. Verifique suas credenciais.');
        }
      }
    );
  }
}
