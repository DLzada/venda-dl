package com.daniel.loja_dl_api.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExcepitonHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Error> handleEntityNotFound(EntityNotFoundException exception){
        HttpStatus status = HttpStatus.NOT_FOUND;
        Error error = new Error(status.value(), exception.getMessage());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Error> handleBusiness(BusinessException exception){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Error error = new Error(status.value(), exception.getMessage());
        return ResponseEntity.status(status).body(error);
    }
}
