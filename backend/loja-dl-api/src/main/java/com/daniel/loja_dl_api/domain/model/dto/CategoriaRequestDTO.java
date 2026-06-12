package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoriaRequestDTO {
    @NotBlank(message = "O nome da categoria é obrigatório.")
    private String nome;
}
