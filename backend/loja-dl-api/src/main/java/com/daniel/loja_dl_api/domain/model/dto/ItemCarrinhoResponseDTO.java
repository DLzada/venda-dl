package com.daniel.loja_dl_api.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemCarrinhoResponseDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;
    private BigDecimal subtotal;


}
