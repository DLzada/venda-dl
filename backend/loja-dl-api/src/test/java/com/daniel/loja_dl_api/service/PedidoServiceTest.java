package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.ItemPedido;
import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    @DisplayName("Deve cancelar um pedido aguardando pagamento com sucesso e devolver itens ao estoque")
    void cancelarPedidoCenario1(){
        Long pedidoId = 1L;

        Produto produtoSimulado = new Produto();
        produtoSimulado.setId(50L);
        produtoSimulado.setQuantidadeEstoque(10);

        ItemPedido itemSimulado = new ItemPedido();
        itemSimulado.setProduto(produtoSimulado);
        itemSimulado.setQuantidade(2);

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setItens(List.of(itemSimulado));

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSimulado);

        pedidoService.cancelarPedido(pedidoId);

        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        assertEquals(12, produtoSimulado.getQuantidadeEstoque());

        verify(produtoRepository, times(1)).save(any(Produto.class));
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Deve mudar o status do pedido para PAGO quando o Webhook receber a confirmação")
    void confirmarPagamentoCenario1(){
        Long pedidoId = 2L;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);

        pedidoService.processarPagamento(pedidoId);

        assertEquals(StatusPedido.PAGO, pedido.getStatus());

        verify(produtoService, never()).reabastecerEstoque(anyLong(), anyInt());
        verify(pedidoRepository, times(1)).save(pedido);
    }

    @Test
    @DisplayName("Deve lançar BusinessException ao tentar cancelar um pedido que já esta pago")
    void cancelarPedidoCenario2(){
        Long pedidoId = 3L;

        Pedido pedido = new Pedido();
        pedido.setId(pedidoId);
        pedido.setStatus(StatusPedido.PAGO);

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.of(pedido));

        assertThrows(com.daniel.loja_dl_api.infra.exception.BusinessException.class, () -> {
            pedidoService.cancelarPedido(pedidoId);
        });

        verify(produtoService, never()).reabastecerEstoque(anyLong(), anyInt());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}
