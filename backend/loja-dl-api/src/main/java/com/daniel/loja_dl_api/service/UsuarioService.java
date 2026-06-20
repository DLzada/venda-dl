package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.UsuarioAtualizacaoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.UsuarioResponseDTO;
import com.daniel.loja_dl_api.domain.model.entity.Usuario;
import com.daniel.loja_dl_api.domain.repository.UsuarioRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioResponseDTO buscarUsuarioLogado(){
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new UsuarioResponseDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getPerfil().name()
        );
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuarioLogado(UsuarioAtualizacaoRequestDTO requestDTO){
        Usuario usuarioLogado = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!usuarioLogado.getEmail().equals(requestDTO.getEmail()) && usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BusinessException("Este e-mail já está em uso por outro usuário!");
        }

        usuarioLogado.setNome(requestDTO.getNome().trim());
        usuarioLogado.setEmail(requestDTO.getEmail().trim());

        usuarioRepository.save(usuarioLogado);

        return new UsuarioResponseDTO(
                usuarioLogado.getNome(),
                usuarioLogado.getEmail(),
                usuarioLogado.getPerfil().name()
        );
    }
}
