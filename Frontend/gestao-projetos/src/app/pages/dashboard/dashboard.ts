import {Component} from '@angular/core';
import {Navbar} from '../../shared/navbar/navbar';
import {Sidebar} from '../../shared/sidebar/sidebar';

@Component({
  selector: 'app-dashboard',
  imports: [
    Navbar,
    Sidebar
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {

}
