import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {Home} from './pages/home/home';
import {Footer} from './components/footer/footer';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Home, Footer],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected title = 'gestao-projetos';
}
