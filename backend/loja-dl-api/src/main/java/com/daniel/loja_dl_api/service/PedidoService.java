package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.Pedido;
import com.daniel.loja_dl_api.domain.model.Produto;
import com.daniel.loja_dl_api.domain.model.StatusPedido;
import com.daniel.loja_dl_api.domain.model.dto.ItemCompraResquestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ItemPedido;
import com.daniel.loja_dl_api.domain.model.dto.PedidoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.PedidoResponseDTO;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public PedidoResponseDTO finalizarCompra(PedidoRequestDTO request){
        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        BigDecimal valorTotalPedido = BigDecimal.ZERO;
        List<ItemPedido> itensPedido = new ArrayList<>();

        for(ItemCompraResquestDTO itemDTO : request.getItens()){
            Produto produto = produtoRepository.findById(itemDTO.getId())
                    .orElseThrow(()-> new RuntimeException("Produto não encontrado"));

            if(produto.getQuantidadeEstoque() < itemDTO.getQuantidade()){
                throw new RuntimeException("Estoque insuficiente para o produto selecionado: " + produto.getNome() + ". Estoque Atual: " + produto.getQuantidadeEstoque());
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDTO.getQuantidade());
            produtoRepository.save(produto);

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemDTO.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedido.setPedido(pedido);

            BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
            valorTotalPedido = valorTotalPedido.add(subTotal);
        }

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotalPedido);
        pedido = pedidoRepository.save(pedido);

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getValorTotal(),
                pedido.getStatus().name()
        );
    }
}
