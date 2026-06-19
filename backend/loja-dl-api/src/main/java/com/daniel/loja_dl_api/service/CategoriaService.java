package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.entity.Categoria;
import com.daniel.loja_dl_api.domain.model.dto.CategoriaRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.CategoriaResponseDTO;
import com.daniel.loja_dl_api.domain.repository.CategoriaRepository;
import com.daniel.loja_dl_api.infra.exception.BusinessException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaResponseDTO criar(CategoriaRequestDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());

        categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }

    @Transactional
    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO requestDTO){
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Categoria não encontrada!"));

        categoria.setNome(requestDTO.getNome().trim());
        categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas(){
        return categoriaRepository.findAll().stream()
                .map(cat -> new CategoriaResponseDTO(cat.getId(), cat.getNome()))
                .collect(Collectors.toList());
    }
}
