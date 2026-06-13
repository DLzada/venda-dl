package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.ProdutoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> criar(@RequestBody @Valid ProdutoRequestDTO requestDTO){
        ProdutoResponseDTO responseDTO = produtoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarTodos(){
        List<ProdutoResponseDTO> lista = produtoService.listarTodos();
        return ResponseEntity.ok(lista);
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
