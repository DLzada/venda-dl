package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Favorito;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.repository.FavoritoRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoritoService {
    private final FavoritoRepository favoritoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public void favoritarProduto(Long produtoId){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(()-> new BusinessException("Produto não encontrado!"));

        if(favoritoRepository.existsByUsuarioAndProduto(usuarioLogado, produto)){
            throw new BusinessException("Este produto já esta na sua lista de favoritos");
        }

        Favorito favorito = new Favorito(usuarioLogado, produto);
        favoritoRepository.save(favorito);
    }

    @Transactional
    public void desfavoritarProduto(Long produtoId){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(()-> new BusinessException("Produto não encontrado!"));

        Favorito favorito = favoritoRepository.findByUsuarioAndProduto(usuarioLogado, produto)
                .orElseThrow(()-> new BusinessException("Este produto não está nos seus favoritos!"));

        favoritoRepository.delete(favorito);
    }

    @Transactional
    public List<ProdutoResponseDTO> listarMeusFavoritos(){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Favorito> favoritos = favoritoRepository.findByUsuario(usuarioLogado.getId());

        return favoritos.stream()
                .map(favorito -> {
                    Produto p = new Produto();
                    ProdutoResponseDTO responseDTO = new ProdutoResponseDTO();

                    responseDTO.setId(p.getId());
                    responseDTO.setPreco(p.getPreco());
                    responseDTO.setNome(p.getNome());
                    responseDTO.setQuantidadeEstoque(p.getQuantidadeEstoque());
                    if (p.getCategoria() == null){
                        responseDTO.setNomeCategoria(p.getCategoria().getNome());
                    }
                    return responseDTO;
                })
                .toList();
    }
}

