package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.CadastroRequestDTO;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.repository.UsuarioRepository;
import com.daniel.loja_dl_api.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AutenticacaoController {
    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    @PostMapping("/cadastro")
    public ResponseEntity<String> registrar(@RequestBody @Valid CadastroRequestDTO requestDTO){
        if(usuarioRepository.findByEmail(requestDTO.getEmail()) != null){
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        String senhaCripto = passwordEncoder.encode(requestDTO.getSenha());
        Usuario novoUsuario = new Usuario(null, requestDTO.getNome(), requestDTO.getEmail(), senhaCripto, requestDTO.getPerfil());

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }
}
