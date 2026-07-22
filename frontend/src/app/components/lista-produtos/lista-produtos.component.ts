import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Produto } from '../../models/produto.model';

@Component({
  selector: 'app-lista-produtos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-produtos.component.html',
  styleUrl: './lista-produtos.component.css'
})
export class ListaProdutosComponent {
  produtos: Produto[] = [
    {
      id: 1,
      nome: "Teclado mecanico RGB",
      descricao: "Teclado com switch blue",
      preco: 299.90,
      quantidadeEstoque: 15,
      categoria: {id:1, nome: "Periféricos"}
    },
    {
      id: 2,
      nome: 'Mouse Gamer Pro',
      descricao: 'Mouse sem fio com sensor de alta precisão Pixart',
      preco: 199.00,
      quantidadeEstoque: 5,
      categoria: { id: 1, nome: 'Periféricos' }
    }
  ];

  comprarProduto(produto:Produto){
    alert(`Voce adicionou ${produto.nome} ao carrinho!`)
  }
}
