package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.dto.abacate.AbacateItemDTO;
import com.daniel.loja_dl_api.domain.model.dto.abacate.AbacatePayRequestDTO;
import com.daniel.loja_dl_api.domain.model.dto.abacate.AbacatePayResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AbacatePayService {
    @Value("${app.gateway.abacate-pay.token}")
    private String TOKEN;

    private final String URL_API = "https://api.abacatepay.com/v1/billing/create";

    public String criarCobrancaPix(Long pedidoId, BigDecimal valorTotal){
        RestTemplate restTemplate = new RestTemplate();

        long valorCentavos = valorTotal.multiply(BigDecimal.valueOf(100)).longValue();

        AbacateItemDTO item = new AbacateItemDTO(pedidoId.toString(), "Pedido Venda#" + pedidoId, 1, valorCentavos);

        AbacatePayRequestDTO requestBody = new AbacatePayRequestDTO(
                1,
                List.of(item),
                "http://localhost:3000/sucesso",
                "http://localhost:3000/concluido"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + TOKEN);

        HttpEntity<AbacatePayRequestDTO> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<AbacatePayResponseDTO> response = restTemplate.exchange(
                    URL_API, HttpMethod.POST, entity, AbacatePayResponseDTO.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().data().url();
            }
            throw new RuntimeException("Falha ao gerar cobrança no Abacate Pay");
        } catch (Exception e) {
            throw new RuntimeException("Erro na integração com o Gateway: " + e.getMessage());
        }
    }
}
