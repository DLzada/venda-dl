package com.daniel.loja_dl_api.domain.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResquestDTO {
    @NotBlank @Email
    private String email;

    @NotBlank
    private String senha;
}
