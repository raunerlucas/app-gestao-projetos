import {Component} from '@angular/core';

@Component({
  selector: 'app-dashboard',
  imports: [
  ],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard {
  selectedTab: string = 'recentes';

  setTab(tab: string) {
    this.selectedTab = tab;
  }
}
