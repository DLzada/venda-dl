package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.ProdutoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Categoria;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.repository.CategoriaRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("Deve criar um produto com sucesso quando os dados forem válidos")
    void criarCenario1(){
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Camiseta DL", "Algodão", new BigDecimal("89.90"), 10, 1L);

        Categoria categoriaSimulada = new Categoria();
        categoriaSimulada.setId(1L);
        categoriaSimulada.setNome("Roupas");

        when(produtoRepository.existsByNome(requestDTO.getNome().trim())).thenReturn(false);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoriaSimulada));

        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(100L);
        produtoSalvo.setNome(requestDTO.getNome());
        produtoSalvo.setPreco(requestDTO.getPreco());
        produtoSalvo.setQuantidadeEstoque(requestDTO.getQuantidadeEstoque());
        produtoSalvo.setCategoria(categoriaSimulada);

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        ProdutoResponseDTO response = produtoService.criar(requestDTO);

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Camiseta DL", response.getNome());

        verify(produtoRepository, times(1)).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve lançar uma BusinessException quando o produto já existir")
    void criarCenario2(){
        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO("Tenis DL", "Corrida", new BigDecimal("299.00"), 5, 1L);

        when(produtoRepository.existsByNome("Tenis DL")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, ()->{
            produtoService.criar(requestDTO);
        });

        assertEquals("Já existe um produto com esse nome!", exception.getMessage());

        verify(produtoRepository, never()).save(any(Produto.class));

    }
}