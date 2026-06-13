package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.Categoria;
import com.daniel.loja_dl_api.domain.model.Produto;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.domain.repository.CategoriaRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO requestDTO){
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada..."));

        Produto produto = new Produto();
        produto.setNome(requestDTO.getNome());
        produto.setDescricao(requestDTO.getDescricao());
        produto.setPreco(requestDTO.getPreco());
        produto.setQuantidadeEstoque(requestDTO.getQuantidadeEstoque());
        produto.setCategoria(categoria);

        produto = produtoRepository.save(produto);

        return converterParaResponse(produto);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos(){
        return produtoRepository.findAll().stream()
                .map(this::converterParaResponse)
                .collect(Collectors.toList());
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
}
