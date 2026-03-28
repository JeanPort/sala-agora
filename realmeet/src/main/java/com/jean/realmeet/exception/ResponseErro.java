package com.jean.realmeet.exception;

import java.time.LocalDateTime;

public record ResponseErro(Integer code, String status, LocalDateTime timestamp, String message) {
}
