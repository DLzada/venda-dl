package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.UsuarioAtualizacaoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.UsuarioResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Categoria;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.repository.CategoriaRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import com.daniel.loja_dl_api.infra.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO requestDTO){
        if(produtoRepository.existsByNome(requestDTO.getNome().trim())){
            throw new BusinessException("Já existe um produto com esse nome!");
        }

        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada..."));

        Produto produto = new Produto();
        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setPreco(requestDTO.getPreco());
        produto.setQuantidadeEstoque(requestDTO.getQuantidadeEstoque());
        produto.setCategoria(categoria);

        produto = produtoRepository.save(produto);

        return converterParaResponse(produto);
    }

//    @Transactional(readOnly = true)
//    public List<ProdutoResponseDTO> listarTodos(){
//        return produtoRepository.findAll().stream()
//                .map(this::converterParaResponse)
//                .collect(Collectors.toList());
//    }

    public Page<ProdutoResponseDTO> listarComFiltros(BigDecimal precoMin, BigDecimal precoMax, Pageable pageable){
        Page<Produto> produtosPage;

        if(precoMin != null && precoMax != null){
            produtosPage = produtoRepository.findByPrecoBetween(precoMin, precoMax, pageable);
        }else {
            produtosPage = produtoRepository.findAll(pageable);
        }

        return produtosPage.map(produto -> {
            ProdutoResponseDTO dto = new ProdutoResponseDTO();
            dto.setId(produto.getId());
            dto.setNome(produto.getNome());
            dto.setPreco(produto.getPreco());
            dto.setQuantidadeEstoque(produto.getQuantidadeEstoque());
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorCategoria(Long categoriaID){
        return produtoRepository.findByCategoriaId(categoriaID).stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> buscarPorNome(String nome){
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
    }

    private ProdutoResponseDTO converterParaResponse(Produto produto){
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getCategoria().getNome()
        );
    }

    @Transactional
    public ProdutoResponseDTO reabastecerEstoque(Long id, int quantidadeParaAdicionar) {
        if (quantidadeParaAdicionar <= 0) {
            throw new BusinessException("A quantidade para adicionar deve ser maior que zero!");
        }

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado para reabastecimento..."));

        int estoqueAtualizado = produto.getQuantidadeEstoque() + quantidadeParaAdicionar;
        produto.setQuantidadeEstoque(estoqueAtualizado);

        produto = produtoRepository.save(produto);

        return converterParaResponse(produto);
    }
}
