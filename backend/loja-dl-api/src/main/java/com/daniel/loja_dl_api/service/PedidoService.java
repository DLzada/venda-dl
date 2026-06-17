package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import com.daniel.loja_dl_api.domain.model.dto.*;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import com.daniel.loja_dl_api.infra.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public PedidoResponseDTO finalizarCompra(PedidoRequestDTO request){

        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setUsuario(usuarioLogado);

        BigDecimal valorTotalPedido = BigDecimal.ZERO;
        List<ItemPedido> itensPedido = new ArrayList<>();

        for(ItemCompraResquestDTO itemDTO : request.getItens()){
            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
                    .orElseThrow(()-> new EntityNotFoundException("Produto não encontrado"));

            if(produto.getQuantidadeEstoque() < itemDTO.getQuantidade()){
                throw new BusinessException("Estoque insuficiente para o produto selecionado: " + produto.getNome() + ". Estoque Atual: " + produto.getQuantidadeEstoque());
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

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos(){
        return pedidoRepository.findAll().stream()
                .map(pedido -> new PedidoResponseDTO(
                        pedido.getId(),
                        pedido.getDataPedido(),
                        pedido.getValorTotal(),
                        pedido.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoDetalhadoResponseDTO buscarPorId(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("O pedido com ID: " + id + " não foi encontrado!"));

        List<ItemPedidoResponseDTO> itensDTO = pedido.getItens().stream()
                .map(item -> new ItemPedidoResponseDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario()
                ))
                .toList();

        return new PedidoDetalhadoResponseDTO(
                pedido.getId(),
                pedido.getDataPedido(),
                pedido.getValorTotal(),
                pedido.getStatus().name(),
                itensDTO
        );
    }
}
