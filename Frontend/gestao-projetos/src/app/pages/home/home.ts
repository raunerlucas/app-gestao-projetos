import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {Header} from '../../shared/header/header';
import {Sidebar} from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-home',
  imports: [
    Sidebar,
    Header,
    RouterOutlet
  ],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home {
}
