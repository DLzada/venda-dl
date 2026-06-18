package com.daniel.loja_dl_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CarrinhoResponseDTO {
    private List<ItemCarrinhoResponseDTO> itens;
    private BigDecimal valorTotal;

    public CarrinhoResponseDTO() {}

    public CarrinhoResponseDTO(List<ItemCarrinhoResponseDTO> itens, BigDecimal valorTotal) {
        this.itens = itens;
        this.valorTotal = valorTotal;
    }
}
