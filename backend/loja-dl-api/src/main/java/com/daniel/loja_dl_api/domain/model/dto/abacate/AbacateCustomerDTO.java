package com.daniel.loja_dl_api.domain.model.dto.abacate;

public record AbacateCustomerDTO(
        String name,
        String email,
        String taxId,
        String cellphone
) {}
