import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ListaProdutosComponent } from "./components/lista-produtos/lista-produtos.component";

@Component({
  selector: 'app-root',
  imports: [ListaProdutosComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'frontend';
}
