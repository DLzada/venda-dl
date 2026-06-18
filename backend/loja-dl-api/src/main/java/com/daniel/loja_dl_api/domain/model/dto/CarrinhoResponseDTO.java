package com.daniel.loja_dl_api.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CarrinhoResponseDTO {
    private List<ItemCarrinhoRequestDTO> itens;
    private BigDecimal valorTotal;
}
