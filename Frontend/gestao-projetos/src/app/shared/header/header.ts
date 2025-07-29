import {Component} from '@angular/core';
import {SessionDataModel} from '../../models/UserSessionModel';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {
  session: string | null;
  userNameSesion: string;
  constructor() {
    this.session = sessionStorage.getItem('userSession');
    if (this.session) {
      const sessionData: SessionDataModel = JSON.parse(this.session);
      this.userNameSesion = sessionData.username;
    } else {
      this.userNameSesion = 'No User';
    }
  }
}
