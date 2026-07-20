package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.ProdutoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Categoria;
import com.daniel.loja_dl_api.domain.model.entity.Produto;
import com.daniel.loja_dl_api.domain.repository.CategoriaRepository;
import com.daniel.loja_dl_api.domain.repository.ProdutoRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import com.daniel.loja_dl_api.infra.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.discovery.DiscoverySelectorIdentifierParser;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.math.BigDecimal;
import java.util.List;
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

    @Test
    @DisplayName("Deve reabastecer o estoque com sucesso quando a quantidade for válida ")
    void reabastecerEstoqueCenario1(){
        Long produtoId = 1L;
        int quantidadeParaAdicionar = 15;

        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");

        Produto produtoExistente = new Produto();
        produtoExistente.setId(produtoId);
        produtoExistente.setNome("Mouse Gamer");
        produtoExistente.setQuantidadeEstoque(10);
        produtoExistente.setCategoria(categoria);

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.of(produtoExistente));

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoExistente);

        ProdutoResponseDTO responseDTO = produtoService.reabastecerEstoque(produtoId, quantidadeParaAdicionar);

        assertNotNull(responseDTO);

        assertEquals(25, responseDTO.getQuantidadeEstoque());

        verify(produtoRepository, times(1)).save(produtoExistente);

    }

    @Test
    @DisplayName("Deve lançar BusinessException quando a quantidade para adicionar for menor ou igual a zero")
    void reabastecerEstoqueCenario2(){
        Long produtoId = 1L;
        int quantidadeValida = 0;

        BusinessException exception = assertThrows(BusinessException.class, ()->{
            produtoService.reabastecerEstoque(produtoId, quantidadeValida);
        });

        assertEquals("A quantidade para adicionar deve ser maior que zero!", exception.getMessage());

        verify(produtoRepository, never()).findById(anyLong());
        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve Lançar EntityNotFoundException quando o produto não for encontrado!")
    void reabatecerEstoqueCenario3(){
        Long produtoId = 999L;
        int quantidade = 10;

        when(produtoRepository.findById(produtoId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, ()->{
            produtoService.reabastecerEstoque(produtoId, quantidade);
        });

        assertEquals("Produto não encontrado para reabastecimento...", exception.getMessage());

        verify(produtoRepository, never()).save(any(Produto.class));
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos quando buscar por nome válido")
    void buscarPorNomeCenario1() {
        String nomeBusca = "Camiseta";

        Categoria categoriaSimulada = new Categoria();
        categoriaSimulada.setId(1L);
        categoriaSimulada.setNome("Roupas");

        Produto p1 = new Produto();
        p1.setId(1L);
        p1.setNome("Camiseta preta DL");
        p1.setCategoria(categoriaSimulada);

        Produto p2 = new Produto();
        p2.setId(2L);
        p2.setNome("Camiseta branca DL");
        p2.setCategoria(categoriaSimulada);

        when(produtoRepository.findByNomeContainingIgnoreCase(nomeBusca))
                .thenReturn(java.util.List.of(p1, p2));

        java.util.List<ProdutoResponseDTO> response = produtoService.buscarPorNome(nomeBusca);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Camiseta preta DL", response.get(0).getNome());
        assertEquals("Camiseta branca DL", response.get(1).getNome());

        verify(produtoRepository, times(1)).findByNomeContainingIgnoreCase(nomeBusca);
    }

    @Test
    @DisplayName("Deve retornar uma pagina de produtos ao listar com filtros e paginação")
    void listarComFiltroCenario1(){
        Pageable pageable = PageRequest.of(0,10);

        Produto p1 = new Produto();
        p1.setId(1L);
        p1.setNome("Tênis DL");
        p1.setPreco(new BigDecimal("199.00"));

        List<Produto> listarProdutos = List.of(p1);

        Page<Produto> paginaSimulada = new PageImpl<>(listarProdutos, pageable, listarProdutos.size());

        when(produtoRepository.findAll(pageable)).thenReturn(paginaSimulada);

        Page<ProdutoResponseDTO> responseDTO = produtoService.listarComFiltros(null, null, pageable);

        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.getTotalElements());
        assertEquals(1, responseDTO.getTotalPages());
        assertEquals("Tênis DL", responseDTO.getContent().get(0).getNome());

        verify(produtoRepository, times(1)).findAll(pageable);
    }
}