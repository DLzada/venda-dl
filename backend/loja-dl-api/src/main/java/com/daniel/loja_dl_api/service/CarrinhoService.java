package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.CarrinhoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.ItemCarrinhoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ItemCarrinhoResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Carrinho;
import com.daniel.loja_dl_api.domain.model.entity.ItemCarrinho;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.repository.CarrinhoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarrinhoService {
    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoRepository produtoRepository;

    private Carrinho obterCarrinhoUsuarioLogado(){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return carrinhoRepository.findByUsuario(usuarioLogado)
                .orElseGet(()-> carrinhoRepository.save(new Carrinho(usuarioLogado)));
    }

    @Transactional
    public CarrinhoResponseDTO adicionarItem(ItemCarrinhoRequestDTO requestDTO){
        Carrinho carrinho = obterCarrinhoUsuarioLogado();

        Produto produto = produtoRepository.findById(requestDTO.getProdutoId())
                .orElseThrow(() -> new BusinessException("Produto nao encontrado!"));

        Optional<ItemCarrinho> itemExistente = carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if(itemExistente.isPresent()){
            ItemCarrinho item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + requestDTO.getQuantidade());
        }else{
            ItemCarrinho novoItem = new ItemCarrinho(carrinho, produto, requestDTO.getQuantidade());
            carrinho.getItens().add(novoItem);
        }

        carrinhoRepository.save(carrinho);
        return buscarCarrinhoDTO();
    }

    public CarrinhoResponseDTO buscarCarrinhoDTO() {
        Carrinho carrinho = obterCarrinhoUsuarioLogado();
        BigDecimal valorTotalGeral = BigDecimal.ZERO;
        List<ItemCarrinhoResponseDTO> itensDTO = new ArrayList<>();

        for (ItemCarrinho item : carrinho.getItens()) {
            BigDecimal subtotal = item.getProduto().getPreco().multiply(new BigDecimal(item.getQuantidade()));
            valorTotalGeral = valorTotalGeral.add(subtotal);

            ItemCarrinhoResponseDTO itemDTO = new ItemCarrinhoResponseDTO();
            itemDTO.setId(item.getId());
            itemDTO.setProdutoId(item.getProduto().getId());
            itemDTO.setNomeProduto(item.getProduto().getNome());
            itemDTO.setPrecoUnitario(item.getProduto().getPreco());
            itemDTO.setQuantidade(item.getQuantidade());
            itemDTO.setSubtotal(subtotal);

            itensDTO.add(itemDTO);
        }

        return new CarrinhoResponseDTO(itensDTO, valorTotalGeral);
    }
}
