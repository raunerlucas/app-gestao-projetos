import {Injectable} from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FloatMenuService {
  private closeAllMenusSubject = new Subject<string>();

  closeAllMenus$ = this.closeAllMenusSubject.asObservable();

  closeAllMenusExcept(menuId: string): void {
    this.closeAllMenusSubject.next(menuId);
  }
}
