import {Component} from '@angular/core';
import {Sidebar} from '../../shared/sidebar/sidebar';
import {Header} from '../../shared/header/header';

@Component({
  selector: 'app-dashboard',
  imports: [
    Sidebar,
    Header
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {

}
