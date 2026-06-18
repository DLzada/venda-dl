package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCarrinhoRequestDTO {
    @NotNull(message = "O id do produto é necessário")
    private Long produtoId;

    @NotNull(message = "A quantidade é obrigatoria!")
    @Positive(message = "A quantidade precisa ser maior que zero!")
    private Integer quantidade;
}
