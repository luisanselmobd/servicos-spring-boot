package com.infnet.clientes.utils;

import com.infnet.clientes.responses.ResponseBase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Hidden
public class ExceptionsHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseBase<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ResponseBase<Map<String, String> > response = new ResponseBase<>(
                errors,
                "Validação falha"
        );

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseBase<Void>> handleResponseStatusException(ResponseStatusException ex) {
        ResponseBase<Void> response = new ResponseBase<>(
                null,
                ex.getReason() != null ? ex.getReason() : "Erro inesperado"
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

}
