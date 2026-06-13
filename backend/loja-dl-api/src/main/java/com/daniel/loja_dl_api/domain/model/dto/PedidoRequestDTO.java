package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {
    @NotEmpty(message = "O carrinho não pode estar vazio!")
    private List<ItemCompraResquestDTO> itens;
}
