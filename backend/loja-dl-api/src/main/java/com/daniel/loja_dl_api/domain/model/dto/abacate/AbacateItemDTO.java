package com.daniel.loja_dl_api.domain.model.dto.abacate;

public record AbacateItemDTO(
        String externalId,
        String name,
        long quantity,
        long price
) {}
