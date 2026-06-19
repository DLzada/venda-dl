package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.CarrinhoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.ItemCarrinhoRequestDTO;
import com.daniel.loja_dl_api.service.CarrinhoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
@PreAuthorize("hasRole('ADMIN') or hasRole('CLIENTE')")
@RequiredArgsConstructor
public class CarrinhoController {
    private final CarrinhoService carrinhoService;

    @GetMapping
    public ResponseEntity<CarrinhoResponseDTO> obterCarrinho(){
        return ResponseEntity.ok(carrinhoService.buscarCarrinhoDTO());
    }

    @PostMapping("/adicionar")
    public ResponseEntity<CarrinhoResponseDTO> adicionarItem(@RequestBody @Valid ItemCarrinhoRequestDTO requestDTO){
        return ResponseEntity.ok(carrinhoService.adicionarItem(requestDTO));
    }

    @DeleteMapping("/remover/{produtoId}")
    public ResponseEntity<CarrinhoResponseDTO> removerItem(@PathVariable Long produtoId){
        return ResponseEntity.ok(carrinhoService.removerItem(produtoId));
    }
}
