package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.ProdutoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody @Valid ProdutoRequestDTO requestDTO){
        ProdutoResponseDTO responseDTO = produtoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarTodos(
            @RequestParam(required = false)BigDecimal precoMin,
            @RequestParam(required = false)BigDecimal precoMax,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable
    ){
        Page<ProdutoResponseDTO> produtos = produtoService.listarComFiltros(precoMin, precoMax, pageable);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/filtrar-categoria")
    public ResponseEntity<List<ProdutoResponseDTO>> filtrarPorCategoria(@RequestParam Long categoriaId){
        List<ProdutoResponseDTO> lista = produtoService.buscarPorCategoria(categoriaId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoResponseDTO>> buscar(@RequestParam String nome){
        List<ProdutoResponseDTO> lista = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(lista);
    }
}
