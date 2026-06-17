package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusPedidoRequestDTO {
    @NotBlank(message = "O status é obrigatório")
    private String status;
}
