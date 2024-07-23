package com.example.bicicletario.bicicletario.exception;

import com.example.bicicletario.bicicletario.domain.dto.ErroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErroDTO> handleInvalidDataException(InvalidDataException e) {
        return createErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErroDTO> handleBadRequestException(BadRequestException e) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<ErroDTO> createErrorResponse(HttpStatus status, String message) {
        ErroDTO error = new ErroDTO(String.valueOf(status.value()), message);
        return new ResponseEntity<>(error, status);
    }
}
