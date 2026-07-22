import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Produto } from '../../models/produto.model';
import { ProdutoService } from '../../services/produto.service';

@Component({
  selector: 'app-lista-produtos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lista-produtos.component.html',
  styleUrl: './lista-produtos.component.css'
})
export class ListaProdutosComponent {
  produtos: Produto[] = [];

  constructor(private produtoService: ProdutoService){}

  ngOnInit(): void{
    this.carregarProdutos();
  }

  carregarProdutos(): void{
    this.produtoService.listarTodos().subscribe({
      next: (dados) => {
        this.produtos = dados;
      },
      error: (erro) =>{
        console.error('Erro ao buscar produtos da API', erro)
      }
    })
  }

  comprarProduto(produto:Produto){
    alert(`Voce adicionou ${produto.nome} ao carrinho!`)
  }
}
