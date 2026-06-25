package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.abacate.AbacateWebhookDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/webhook")
@RequiredArgsConstructor
public class WebhookController {
    private final PedidoService pedidoService;

    @PostMapping("/abacatePay")
    public ResponseEntity<Void> receberWebhookAbacatePay(@RequestBody AbacateWebhookDTO webhookDTO){
        System.out.println("Webhook recebido do abacate Pay! " + webhookDTO.event());

        pedidoService.atualizarStatusViaWebhook(webhookDTO);

        return ResponseEntity.ok().build();
    }
}
