package com.jean.realmeet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e){
        return buildResponseEntity(e, HttpStatus.NOT_FOUND);
    }

    private static ResponseEntity<Object> buildResponseEntity(Exception e, HttpStatus status) {
        var erro = new ResponseErro(status.value(), status.getReasonPhrase(), LocalDateTime.now(), e.getMessage());
        return ResponseEntity.status(status).body(erro);
    }
}
