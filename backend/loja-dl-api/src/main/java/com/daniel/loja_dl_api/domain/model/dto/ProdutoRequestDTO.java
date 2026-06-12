package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProdutoRequestDTO {
    @NotBlank(message = "O nome do Produto é obrigatório!")
    private String nome;

    private String descricao;

    @NotNull(message = "O preço do produto é necessário!")
    @Positive(message = "O preço deve ser maior que zero!")
    private BigDecimal preco;

    @NotNull(message = "Coloque a quantidade do estoque do produto!")
    @PositiveOrZero(message = "A quantidade não pode ser negativo!")
    private Integer quantidadeEstoque;

    @NotNull(message = "O id da categoria é obrigatório!")
    private Long categoriaId;
}
