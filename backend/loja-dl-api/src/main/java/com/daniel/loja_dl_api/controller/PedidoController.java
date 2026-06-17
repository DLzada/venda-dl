package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.PedidoDetalhadoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.PedidoRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.PedidoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.StatusPedidoRequestDTO;
import com.daniel.loja_dl_api.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<PedidoResponseDTO> efetuarCheckout(@RequestBody @Valid PedidoRequestDTO request){
        PedidoResponseDTO responseDTO = pedidoService.finalizarCompra(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN')")
    public ResponseEntity<List<PedidoResponseDTO>> listarHistorico(){
        List<PedidoResponseDTO> lista = pedidoService.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalhadoResponseDTO> buscarPedido(@PathVariable Long id){
        PedidoDetalhadoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> atulizarStatus(@PathVariable Long id, @RequestBody @Valid StatusPedidoRequestDTO requestDTO){
        pedidoService.atualizarPedido(id, requestDTO.getStatus());
        return ResponseEntity.noContent().build();
    }
}
