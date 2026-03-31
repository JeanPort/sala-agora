package com.jean.realmeet.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class ResponseEntityUtils {

    private ResponseEntityUtils() {
    }

    public static <T> ResponseEntity<T> ok(T body){
        return ResponseEntity.ok(body);
    }

    public static <T> ResponseEntity<T> conflict(T body){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    public static <T> ResponseEntity<T> unprocessableEntity(T body){
        return ResponseEntity.unprocessableEntity().body(body);
    }

    public static <T> ResponseEntity<T> notFound(){
        return ResponseEntity.notFound().build();
    }

    public static <T> ResponseEntity<T> created(T body, Object id){

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(uri).body(body);
    }
}
