package com.daniel.loja_dl_api.domain.model.dto.abacate;

public record AbacateWebhookDTO(
        String event,
        WebhookData data
) {
    public record WebhookData(
            String id,
            String status
    ) {}
}
