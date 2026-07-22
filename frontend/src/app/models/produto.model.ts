import { Categoria } from './categoria.model';

export interface Produto {
  id: number;
  nome: string;
  descricao: string;
  preco: number;
  quantidadeEstoque: number;
  categoria: Categoria;
}