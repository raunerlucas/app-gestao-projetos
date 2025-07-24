import {Component} from '@angular/core';
import {Navbar} from '../../shared/navbar/navbar';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [
    Navbar,
    RouterLink
  ],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

}
