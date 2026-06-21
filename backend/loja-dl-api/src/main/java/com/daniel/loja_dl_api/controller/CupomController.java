package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.CupomRequestDTO;
import com.daniel.loja_dl_api.domain.model.entity.Cupom;
import com.daniel.loja_dl_api.domain.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cupons")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class CupomController {
    private final CupomRepository cupomRepository;

    @PostMapping
    public ResponseEntity<Cupom> criarCupom(@RequestBody CupomRequestDTO requestDTO){
        Cupom cupom = new Cupom();
        cupom.setCodigo(requestDTO.getCodigo().toUpperCase().trim());
        cupom.setDataValidade(requestDTO.getDataValidade());
        cupom.setPorcentagemDesconto(requestDTO.getPorcentagemDesconto());

        return ResponseEntity.status(HttpStatus.CREATED).body(cupomRepository.save(cupom));
    }

    @GetMapping
    public ResponseEntity<List<Cupom>> listar(){
        return ResponseEntity.ok(cupomRepository.findAll());
    }
}
