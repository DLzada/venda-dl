package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.PedidoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.PedidoResponseDTO;
import com.daniel.loja_dl_api.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> efetuarCheckout(@RequestBody @Valid PedidoRequestDTO request){
        PedidoResponseDTO responseDTO = pedidoService.finalizarCompra(request);
        ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
