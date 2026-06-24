package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.CadastroRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.LoginResquestDTO;
import com.daniel.loja_dl_api.domain.model.dto.TokenResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.UsuarioResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.model.enums.Perfil;
import com.daniel.loja_dl_api.domain.repository.UsuarioRepository;
import com.daniel.loja_dl_api.infra.security.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
        if (usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        String senhaCripto = passwordEncoder.encode(requestDTO.getSenha());
        Usuario novoUsuario = new Usuario(null, requestDTO.getNome(), requestDTO.getEmail(), senhaCripto, Perfil.CLIENTE);

        usuarioRepository.save(novoUsuario);
        return ResponseEntity.ok("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@RequestBody @Valid LoginResquestDTO dto){
        var dadosLogin = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha());
        var authetication = authenticationManager.authenticate(dadosLogin);

        String tokenJWT = tokenService.gerarToken((Usuario) authetication.getPrincipal());

        return ResponseEntity.ok(new TokenResponseDTO(tokenJWT));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obterUsuarioLogado(){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                usuarioLogado.getPerfil().name()
        );

        return ResponseEntity.ok(response);
    }
}
