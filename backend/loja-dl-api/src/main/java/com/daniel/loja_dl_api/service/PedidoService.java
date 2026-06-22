package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.entity.*;
import com.daniel.loja_dl_api.domain.model.enums.Perfil;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import com.daniel.loja_dl_api.domain.model.dto.*;
import com.daniel.loja_dl_api.domain.repository.CarrinhoRepository;
import com.daniel.loja_dl_api.domain.repository.CupomRepository;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import com.daniel.loja_dl_api.infra.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final CarrinhoRepository carrinhoRepository;
    private final CupomRepository cupomRepository;

//    @Transactional
//    public PedidoResponseDTO finalizarCompra(PedidoRequestDTO request){
//
//        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        Pedido pedido = new Pedido();
//        pedido.setDataPedido(LocalDateTime.now());
//        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
//        pedido.setUsuario(usuarioLogado);
//
//        BigDecimal valorTotalPedido = BigDecimal.ZERO;
//        List<ItemPedido> itensPedido = new ArrayList<>();
//
//        for(ItemCompraResquestDTO itemDTO : request.getItens()){
//            Produto produto = produtoRepository.findById(itemDTO.getProdutoId())
//                    .orElseThrow(()-> new EntityNotFoundException("Produto não encontrado"));
//
//            if(produto.getQuantidadeEstoque() < itemDTO.getQuantidade()){
//                throw new BusinessException("Estoque insuficiente para o produto selecionado: " + produto.getNome() + ". Estoque Atual: " + produto.getQuantidadeEstoque());
//            }
//
//            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemDTO.getQuantidade());
//            produtoRepository.save(produto);
//
//            ItemPedido itemPedido = new ItemPedido();
//            itemPedido.setProduto(produto);
//            itemPedido.setQuantidade(itemDTO.getQuantidade());
//            itemPedido.setPrecoUnitario(produto.getPreco());
//            itemPedido.setPedido(pedido);
//
//            BigDecimal subTotal = produto.getPreco().multiply(BigDecimal.valueOf(itemDTO.getQuantidade()));
//            valorTotalPedido = valorTotalPedido.add(subTotal);
//        }
//
//        pedido.setItens(itensPedido);
//        pedido.setValorTotal(valorTotalPedido);
//        pedido = pedidoRepository.save(pedido);
//
//        return new PedidoResponseDTO(
//                pedido.getId(),
//                pedido.getDataPedido(),
//                pedido.getValorTotal(),
//                pedido.getStatus().name()
//        );
//    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarTodos(){
        List<Pedido> pedidos = pedidoRepository.findAll();

        return pedidos.stream()
                .map(pedido -> {
                    PedidoResponseDTO dto = new PedidoResponseDTO();
                    dto.setId(pedido.getId());
                    dto.setDataPedido(pedido.getDataPedido());
                    dto.setStatus(pedido.getStatus().name());
                    dto.setValorTotal(pedido.getValorTotal());

                    return dto;
                })
                .toList();
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

    @Transactional
    public void atualizarPedido(Long pedidoId, String novoStatusStr){

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado!"));

        try {
            StatusPedido novoStatus = StatusPedido.valueOf(novoStatusStr.toUpperCase());

            pedido.setStatus(novoStatus);
            pedidoRepository.save(pedido);
        }catch (IllegalArgumentException e){
            throw new BusinessException("Status inválido!");
        }
    }

    @Transactional
    public PedidoResponseDTO finalizarCompraCarrinho(String codigoCupom){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Carrinho carrinho = carrinhoRepository.findByUsuario(usuarioLogado)
                .orElseThrow(()-> new BusinessException("Voce não possui um carrinho!"));

        if(carrinho.getItens().isEmpty()){
            throw new BusinessException("Seu carrinho está vazio!");
        }

        Pedido pedido = new Pedido();
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setUsuario(usuarioLogado);

        List<ItemPedido> itensPedido = new ArrayList<>();
        BigDecimal valorTotalPedido = BigDecimal.ZERO;

        for(ItemCarrinho itemCarrinho: carrinho.getItens()){
            Produto produto = itemCarrinho.getProduto();

            if(produto.getQuantidadeEstoque() < itemCarrinho.getQuantidade()){
                throw new BusinessException("Estoque insufisiente do produto!");
            }

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - itemCarrinho.getQuantidade());

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            itemPedido.setQuantidade(itemCarrinho.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());

            BigDecimal subtotal = produto.getPreco().multiply(new BigDecimal(itemCarrinho.getQuantidade()));
            valorTotalPedido = valorTotalPedido.add(subtotal);

            itensPedido.add(itemPedido);
        }

        if (codigoCupom != null && !codigoCupom.trim().isEmpty()) {
            Cupom cupom = cupomRepository.findByCodigoIgnoreCase(codigoCupom.trim())
                    .orElseThrow(() -> new BusinessException("Cupom de desconto inválido!"));

            if (cupom.getDataValidade().isBefore(LocalDate.now())) {
                throw new BusinessException("Este cupom já está expirado!");
            }

            BigDecimal desconto = valorTotalPedido.multiply(cupom.getPorcentagemDesconto())
                    .divide(BigDecimal.valueOf(100));


            valorTotalPedido = valorTotalPedido.subtract(desconto);
        }

        pedido.setItens(itensPedido);
        pedido.setValorTotal(valorTotalPedido);

        pedidoRepository.save(pedido);

        carrinho.getItens().clear();
        carrinhoRepository.save(carrinho);

        return converterParaPedidoResponseDTO(pedido);
    }

    private PedidoResponseDTO converterParaPedidoResponseDTO(Pedido pedido){
        PedidoResponseDTO responseDTO = new PedidoResponseDTO();
        responseDTO.setId(pedido.getId());
        responseDTO.setDataPedido(pedido.getDataPedido());
        responseDTO.setStatus(pedido.getStatus().name());
        responseDTO.setValorTotal(pedido.getValorTotal());

        return responseDTO;
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> listarMeusPedidos(){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdOrderByDataPedidoDesc(usuarioLogado.getId());

        return pedidos.stream()
                .map(pedido -> {
                    PedidoResponseDTO dto = new PedidoResponseDTO();
                    dto.setId(pedido.getId());
                    dto.setDataPedido(pedido.getDataPedido());
                    dto.setStatus(pedido.getStatus().name());
                    dto.setValorTotal(pedido.getValorTotal());

                    return dto;
                })
                .toList();
    }

    @Transactional
    public void cancelarPedido(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pedido não encontrado com o ID: " + id));

        if(pedido.getStatus() == StatusPedido.CANCELADO){
            throw new BusinessException("Este pedido já se encontra cancelado");
        }

        for (ItemPedido item : pedido.getItens()) {
            Produto produto = item.getProduto();

            produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void processarPagamento(Long pedidoId){
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(()-> new BusinessException("Pedido não encontrado!"));

        if (pedido.getStatus()== StatusPedido.CANCELADO){
            throw new BusinessException("Não foi possivel pagar o pedido que já foi cancelado!");
        }

        if (pedido.getStatus() == StatusPedido.PAGO){
            throw new BusinessException("Pedido já foi pago!");
        }

        pedido.setStatus(StatusPedido.PAGO);
        pedidoRepository.save(pedido);
    }
}
