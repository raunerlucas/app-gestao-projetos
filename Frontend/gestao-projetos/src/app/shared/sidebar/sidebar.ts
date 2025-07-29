import {Component} from '@angular/core';
import {Logo} from '../logo/logo';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.html',
  imports: [
    Logo
  ],
  styleUrls: ['./sidebar.css']
})
export class Sidebar {
}
