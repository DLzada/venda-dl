package com.daniel.loja_dl_api.domain.model.dto;

import com.daniel.loja_dl_api.domain.model.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroRequestDTO {
    @NotBlank(message = "o nome é obrigatório!")
    private String nome;

    @NotBlank(message = "Email é obrigatório!")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotNull(message = "Perfil é obrigatório")
    private Perfil perfil;
}
