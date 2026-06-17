package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.CategoriaRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.CategoriaResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoriaResponseDTO> cadastrar(@RequestBody @Valid CategoriaRequestDTO dto){
        CategoriaResponseDTO responseDTO = categoriaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas(){
        List <CategoriaResponseDTO> lista = categoriaService.listarTodas();
        return ResponseEntity.ok(lista);
    }
}
