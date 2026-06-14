package com.daniel.loja_dl_api.infra.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Error {
    private Integer status;
    private LocalDateTime dataHora;
    private String mensagem;
}
