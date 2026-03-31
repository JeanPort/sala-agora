package com.jean.realmeet.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseErro(Integer code, String status, LocalDateTime timestamp, String message, List<FieldErro> errors) {

    public record FieldErro(String field, String fieldMessage){}
}

