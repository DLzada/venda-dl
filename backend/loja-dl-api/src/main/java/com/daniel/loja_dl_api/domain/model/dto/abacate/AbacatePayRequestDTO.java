package com.daniel.loja_dl_api.domain.model.dto.abacate;

import java.util.List;

public record AbacatePayRequestDTO(
        long frequency,
        List<AbacateItemDTO> products,
        String returnUrl,
        String completionUrl
) {}
