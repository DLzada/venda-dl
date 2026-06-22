package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.DashboardResumoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.ItemPedido;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public DashboardResumoResponseDTO obterResumoVendas(LocalDate dataInicio, LocalDate dataFim){
        entityManager.clear();

        List<Pedido> pedidosPagos;

        if (dataInicio != null && dataFim != null) {
            LocalDateTime inicio = dataInicio.atStartOfDay();
            LocalDateTime fim = dataFim.atTime(23, 59, 59);
            pedidosPagos = pedidoRepository.findByStatusAndDataPedidoBetween(StatusPedido.PAGO, inicio, fim);
        } else {
            pedidosPagos = pedidoRepository.findByStatus(StatusPedido.PAGO);
        }

        BigDecimal faturamentoTotal = BigDecimal.ZERO;
        Map<String, Integer> contagemProdutos = new HashMap<>();

        for (Pedido pedido : pedidosPagos){
            faturamentoTotal = faturamentoTotal.add(pedido.getValorTotal());

            for (ItemPedido item : pedido.getItens()){
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

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarAlertasEstoqueBaixo(){
        int limiteCritico = 5;
        List<Produto> produtosCriticos = produtoRepository.findByQuantidadeEstoqueLessThanEqual(limiteCritico);

        return produtosCriticos.stream()
                .map(produto -> {
                    ProdutoResponseDTO responseDTO = new ProdutoResponseDTO();
                    responseDTO.setId(produto.getId());
                    responseDTO.setNome(produto.getNome());
                    responseDTO.setPreco(produto.getPreco());
                    responseDTO.setQuantidadeEstoque(produto.getQuantidadeEstoque());
                    if(produto.getCategoria() != null){
                        responseDTO.setNomeCategoria(produto.getCategoria().getNome());
                    }
                    return responseDTO;
                })
                .toList();
    }

}
