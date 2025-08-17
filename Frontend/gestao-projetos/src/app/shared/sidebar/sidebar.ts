import {Component, Inject, PLATFORM_ID} from '@angular/core';
import {isPlatformBrowser} from '@angular/common';
import {Logo} from '../logo/logo';
import {SessionDataModel} from '../../models/UserSessionModel';
import {Auth} from '../../core/services/auth/auth';
import {Router, RouterLink, RouterLinkActive} from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.html',
  imports: [
    Logo,
    RouterLink,
    RouterLinkActive
  ],
  styleUrls: ['./sidebar.css']
})
export class Sidebar {
  session: string | null = null;
  userNameSesion: string = 'No User';

  constructor(
    private auth: Auth,
    private routes: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    if (isPlatformBrowser(this.platformId)) {
      this.session = sessionStorage.getItem('userSession');
      if (this.session) {
        const sessionData: SessionDataModel = JSON.parse(this.session);
        this.userNameSesion = sessionData.username;
      }
    }
  }

  logout() {
    this.auth.logout();
    this.routes.navigate(['/login']);
  }
}
