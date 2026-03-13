package com.joseag.coldroommonitor.infrastructure.exception;

import com.joseag.coldroommonitor.domain.exceptions.ColdRoomNotFoundException;
import com.joseag.coldroommonitor.domain.exceptions.SensorDeviceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String COLD_ROOM_NOT_FOUND = "COLD_ROOM_NOT_FOUND";
    private static final String SENSOR_DEVICE_NOT_FOUND = "SENSOR_DEVICE_NOT_FOUND";
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String MESSAGE_NOT_READABLE = "MALFORMED_JSON_REQUEST";
    private static final String DATA_INTEGRITY_VIOLATION = "DATA_INTEGRITY_VIOLATION";
    private static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": "+error.getDefaultMessage())
                .findFirst().orElse("Invalid request");

        return buildApiError(
                HttpStatus.BAD_REQUEST,
                VALIDATION_ERROR,
                message,
                request
        );
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraints(
            ConstraintViolationException ex,
            HttpServletRequest request){

        String message = ex.getConstraintViolations().stream()
                .map(error -> error.getPropertyPath().toString()
                        .replaceAll("^.*\\.", "") + ": "+error.getMessage())
                .findFirst().orElse("Invalid request");

        return buildApiError(
                HttpStatus.BAD_REQUEST,
                VALIDATION_ERROR,
                message,
                request
        );
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request){

        return buildApiError(
                HttpStatus.BAD_REQUEST,
                MESSAGE_NOT_READABLE,
                "Malformed JSON request",
                request
        );
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request){

        return buildApiError(
                HttpStatus.BAD_REQUEST,
                VALIDATION_ERROR,
                ex.getMessage(),
                request
        );
    }


    @ExceptionHandler(ColdRoomNotFoundException.class)
    public ResponseEntity<ApiError> handleColdRoomNotFound(
            ColdRoomNotFoundException ex,
            HttpServletRequest request){

        return buildApiError(
                HttpStatus.NOT_FOUND,
                COLD_ROOM_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }


    @ExceptionHandler(SensorDeviceNotFoundException.class)
    public ResponseEntity<ApiError> handleSensorDeviceNotFound(
            SensorDeviceNotFoundException ex,
            HttpServletRequest request){

        return buildApiError(
                HttpStatus.NOT_FOUND,
                SENSOR_DEVICE_NOT_FOUND,
                ex.getMessage(),
                request
        );
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request){

        return buildApiError(
                HttpStatus.CONFLICT,
                DATA_INTEGRITY_VIOLATION,
                "Database integrity violation",
                request
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception ex,
            HttpServletRequest request){

        return buildApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request
        );
    }


    private ResponseEntity<ApiError> buildApiError(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request){
        ApiError apiError = new ApiError(
                status.value(),
                error,
                message,
                request.getRequestURI(),
                LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
        );

        return ResponseEntity.status(status).body(apiError);
    }

}
