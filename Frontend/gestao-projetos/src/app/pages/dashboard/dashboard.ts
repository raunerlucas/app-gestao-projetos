import {Component} from '@angular/core';
import {Sidebar} from '../../shared/sidebar/sidebar';
import {Header} from '../../shared/header/header';
import {RouterOutlet} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [
    Sidebar,
    Header,
    RouterOutlet
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {

}
