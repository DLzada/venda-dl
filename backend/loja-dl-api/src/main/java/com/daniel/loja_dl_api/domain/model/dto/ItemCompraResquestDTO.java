package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemCompraResquestDTO {

    @NotNull(message = "O ID do produto é obrigatorio")
    private Long produtoId;

    @NotNull(message = "É necessário informar uma quantidade!")
    @Positive(message = "A quantidade precisa ser maior que zero")
    private Integer quantidade;
}
