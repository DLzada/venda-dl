package com.daniel.loja_dl_api.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Error {
    private Integer status;
    private LocalDateTime dataHora;
    private String mensagem;

    public Error(Integer status, String mensagem) {
        this.status = status;
        this.dataHora = LocalDateTime.now();
        this.mensagem = mensagem;
    }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
