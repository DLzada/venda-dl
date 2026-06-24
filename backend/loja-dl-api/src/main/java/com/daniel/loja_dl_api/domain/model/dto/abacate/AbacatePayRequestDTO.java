package com.daniel.loja_dl_api.domain.model.dto.abacate;

import java.util.List;

public record AbacatePayRequestDTO(
        String frequency,
        List<AbacateItemDTO> products,
        AbacateCustomerDTO customer,
        List<String> methods,
        String returnUrl,
        String completionUrl
) {}
