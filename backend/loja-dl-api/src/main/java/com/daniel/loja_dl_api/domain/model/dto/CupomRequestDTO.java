package com.daniel.loja_dl_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CupomRequestDTO {
    private String codigo;
    private BigDecimal porcentagemDesconto;
    private LocalDate dataValidade;
}
