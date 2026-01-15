package com.joseag.coldroommonitor.infrastructure.exception;

import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String COLD_ROOM_NOT_FOUND = "COLD_ROOM_NOT_FOUND";
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    @ExceptionHandler(ColdRoomNotFoundException.class)
    public ResponseEntity<ApiError> handleColdRoomNotFound(ColdRoomNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(COLD_ROOM_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "+error.getDefaultMessage())
                .findFirst().orElse("Invalid request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(VALIDATION_ERROR, message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraints(ConstraintViolationException ex){

        String message = ex.getConstraintViolations().stream()
                .map(error -> error.getPropertyPath().toString()
                        .replaceAll("^.*\\.", "") + ": "+error.getMessage())
                .findFirst().orElse("Invalid request");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(VALIDATION_ERROR, message));
    }


}
