package com.joseag.coldroommonitor.infrastructure.exception;

import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ColdRoomNotFoundException.class)
    public ResponseEntity<String> handleColdRoomNotFound(ColdRoomNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
