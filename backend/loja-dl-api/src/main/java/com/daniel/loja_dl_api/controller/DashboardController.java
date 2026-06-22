package com.daniel.loja_dl_api.controller;

import com.daniel.loja_dl_api.domain.model.dto.DashboardResumoResponseDTO;
import com.daniel.loja_dl_api.domain.model.dto.ProdutoResponseDTO;
import com.daniel.loja_dl_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/resumo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardResumoResponseDTO> obterResumo(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim){

        DashboardResumoResponseDTO resumo = dashboardService.obterResumoVendas(dataInicio, dataFim);
        return ResponseEntity.ok(resumo);
    }

    @GetMapping("/alertas-estoque")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProdutoResponseDTO>> obterAlertasEstoques(){
        List<ProdutoResponseDTO> alertas = dashboardService.listarAlertasEstoqueBaixo();
        return ResponseEntity.ok(alertas);
    }
}
