package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.DashboardResumoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.ItemPedido;
import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PedidoRepository pedidoRepository;

    public DashboardResumoResponseDTO obterResumoVendas(){
        List<Pedido> pedidosPagos = pedidoRepository.findByStatus(StatusPedido.PAGO);

        BigDecimal faturamentoTotal = BigDecimal.ZERO;
        Map<String, Integer> contagemProdutos = new HashMap<>();

        for (Pedido pedido: pedidosPagos){
            faturamentoTotal = faturamentoTotal.add(pedido.getValorTotal());

            for (ItemPedido item: pedido.getItens()){
                String nomeproduto = item.getProduto().getNome();
                int qtdComprada = item.getQuantidade();

                contagemProdutos.put(nomeproduto, contagemProdutos.getOrDefault(nomeproduto, 0) + qtdComprada);
            }
        }

        String produtoCampeao = contagemProdutos.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Nenhuma venda realizada");

        long totalPedidosConcluidos = pedidosPagos.size();

        return new DashboardResumoResponseDTO(faturamentoTotal, totalPedidosConcluidos, produtoCampeao);
    }
}
