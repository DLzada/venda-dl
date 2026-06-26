package com.daniel.loja_dl_api.service;

import com.daniel.loja_dl_api.domain.model.entity.Pedido;
import com.daniel.loja_dl_api.domain.model.enums.StatusPedido;
import com.daniel.loja_dl_api.domain.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PedidoAgendadorService {
    private final PedidoRepository pedidoRepository;
    private final PedidoService pedidoService;

    @Scheduled(fixedDelay = 60000)
    public void cancelarPedidosExpirados(){
        System.out.println("Verificando se existem Pix expirados ou abandonados");

        LocalDateTime tempoLimite = LocalDateTime.now().plusMinutes(15);

        List<Pedido> pedidosExpirados = pedidoRepository.findByStatusAndDataPedidoBefore(StatusPedido.AGUARDANDO_PAGAMENTO, tempoLimite);

        if(!pedidosExpirados.isEmpty()){
            System.out.println("Foram encontrado(s) " + pedidosExpirados.size() + " pedido(s) abandonado(s). Cancelando...");

            for (Pedido pedido: pedidosExpirados){
                try {
                    pedidoService.cancelarPedido(pedido.getId());
                    System.out.println("Pedido " + pedido.getId() + " cancelado e devolvido para o estoque.");
                }catch (Exception e){
                    System.out.println("Erro ao cancelar o pedido " + pedido.getId());
                }
            }
        }else {
            System.out.println("Tudo limpo por aqui, nenhum pedido abandonado");
        }
    }
}
