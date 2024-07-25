package com.example.bicicletario.bicicletario.application.exceptions;

import com.example.bicicletario.bicicletario.domain.models.Erro;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Erro> handleResourceNotFoundException(ResourceNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Erro> handleInvalidDataException(InvalidDataException e) {
        return createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Erro> handleBadRequestException(BadRequestException e) {
        return createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Erro> handleRuntimeException(RuntimeException ex) {
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<Erro> createErrorResponse(HttpStatus status, String message) {
        Erro error = new Erro(String.valueOf(status.value()), message);
        return new ResponseEntity<>(error, status);
    }
}