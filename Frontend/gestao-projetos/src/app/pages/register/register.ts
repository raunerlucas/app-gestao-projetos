import {Component} from '@angular/core';
import {Navbar} from '../../shared/navbar/navbar';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [
    Navbar,
    RouterLink
  ],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

}
