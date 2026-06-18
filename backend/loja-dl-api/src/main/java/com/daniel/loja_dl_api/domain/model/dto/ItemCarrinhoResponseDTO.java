package com.daniel.loja_dl_api.domain.model.dto;

import java.math.BigDecimal;

public class ItemCarrinhoResponseDTO {
    private Long id;
    private Long produtoId;
    private String nomeProduto;
    private BigDecimal precoUnitario;
    private Integer quantidade;
    private BigDecimal subTotal;
}
