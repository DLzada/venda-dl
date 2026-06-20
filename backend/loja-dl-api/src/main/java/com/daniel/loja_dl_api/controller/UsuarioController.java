package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.UsuarioAtualizacaoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.UsuarioResponseDTO;
import com.daniel.loja_dl_api.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> obterPerfil(){
        return ResponseEntity.ok(usuarioService.buscarUsuarioLogado());
    }

    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(@RequestBody @Valid UsuarioAtualizacaoRequestDTO requestDTO){
        return ResponseEntity.ok(usuarioService.atualizarUsuarioLogado(requestDTO));
    }
}
