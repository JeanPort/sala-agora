package com.jean.realmeet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static com.jean.realmeet.util.ResponseEntityUtils.conflict;
import static com.jean.realmeet.util.ResponseEntityUtils.notFound;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e){
        return notFound();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Object> handleConflictException(ConflictException e){
        var status = HttpStatus.CONFLICT;
        return conflict(new ResponseErro(
                status.value(),
                status.getReasonPhrase(),
                LocalDateTime.now(),
                e.getMessage(),
                null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        var erros = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ResponseErro.FieldErro(fieldError.getField(), fieldError.getDefaultMessage()))
                .toList();
        var status = HttpStatus.UNPROCESSABLE_ENTITY;
        return ResponseEntity.unprocessableEntity().body(new ResponseErro(status.value(),
                status.getReasonPhrase(),
                LocalDateTime.now(),
                "Validation failed",
                erros));
    }

}
