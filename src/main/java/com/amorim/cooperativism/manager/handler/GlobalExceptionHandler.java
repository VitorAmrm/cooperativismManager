package com.amorim.cooperativism.manager.handler;

import com.amorim.cooperativism.manager.domain.to.ApplicationResponse;
import com.amorim.cooperativism.manager.feign.ServiceNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(new ApplicationResponse(String.join("#", errors.values()), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ServiceNotAvailableException.class)
    public ResponseEntity<ApplicationResponse> handleNotAvailable(ServiceNotAvailableException ex) {
        return new ResponseEntity<>(new ApplicationResponse("Serviço de Validação de CPF não disponivel no momento", HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }
}
