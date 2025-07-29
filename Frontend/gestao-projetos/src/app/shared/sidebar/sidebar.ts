import {Component} from '@angular/core';
import {Logo} from '../logo/logo';
import {SessionDataModel} from '../../models/UserSessionModel';
import {Auth} from '../../core/services/auth';
import {Router} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.html',
  imports: [
    Logo
  ],
  styleUrls: ['./sidebar.css']
})
export class Sidebar {
  session: string | null;
  userNameSesion: string;
  constructor(private auth: Auth, private routes: Router) {
    this.session = sessionStorage.getItem('userSession');
    if (this.session) {
      const sessionData: SessionDataModel = JSON.parse(this.session);
      this.userNameSesion = sessionData.username;
    } else {
      this.userNameSesion = 'No User';
    }
  }

  logout() {
    this.auth.logout();
    this.routes.navigate(['/login']);
  }
}
