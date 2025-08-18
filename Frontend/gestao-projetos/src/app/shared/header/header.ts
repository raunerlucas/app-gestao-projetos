import {Component, Inject, PLATFORM_ID} from '@angular/core';
import {SessionDataModel} from '../../models/UserSession.model';
import {isPlatformBrowser} from '@angular/common';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.html',
  styleUrl: './header.css'
})
export class Header {
  session: string | null;
  userNameSesion: string;


  constructor(@Inject(PLATFORM_ID) private platformId: Object) {
    if (isPlatformBrowser(this.platformId)) {
      this.session = sessionStorage.getItem('userSession');
      if (this.session) {
        const sessionData: SessionDataModel = JSON.parse(this.session);
        this.userNameSesion = sessionData.username;
      } else {
        this.userNameSesion = 'No User';
      }
    } else {
      this.session = null;
      this.userNameSesion = 'No User';
    }
  }
}
