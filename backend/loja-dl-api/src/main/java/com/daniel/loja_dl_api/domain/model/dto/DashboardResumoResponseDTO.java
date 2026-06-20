package com.daniel.loja_dl_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResumoResponseDTO {
    private BigDecimal faturamentoTotal;
    private Long totalPedidos;
    private String produtoMaisVendido;
}
