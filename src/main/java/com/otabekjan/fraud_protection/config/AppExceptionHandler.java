package com.otabekjan.fraud_protection.config;

import com.otabekjan.fraud_protection.exceptions.FriendlyException;
import io.jmix.core.security.AccessDeniedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = FriendlyException.class)
    public ResponseEntity<?> handleException(FriendlyException friendlyException) {
        record details(String friendlyMessage, int status) {
        }

        return ResponseEntity.badRequest()
                .body(new details(friendlyException.getMessage(), 400));
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleAccessException(AccessDeniedException accessDeniedException) {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> "%s %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}
