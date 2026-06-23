package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.service.FavoritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {
    private final FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> listarFavoritos(){
        return ResponseEntity.ok(favoritoService.listarMeusFavoritos());
    }

    @PostMapping("/{produtoId}")
    public ResponseEntity<Void> favoritar(@PathVariable Long produtoId){
        favoritoService.favoritarProduto(produtoId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{produtoId}")
    public ResponseEntity<Void> desfavoritar(@PathVariable Long produtoId){
        favoritoService.desfavoritarProduto(produtoId);
        return ResponseEntity.noContent().build();
    }
}
