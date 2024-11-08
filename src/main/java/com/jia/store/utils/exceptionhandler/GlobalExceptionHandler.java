package com.jia.store.utils.exceptionhandler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<GlobalExceptionResponse> globalHandler(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new GlobalExceptionResponse(
                (HttpStatus) e.getStatusCode(), e.getReason()
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalExceptionResponse> globalHandler(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GlobalExceptionResponse(
                HttpStatus.BAD_REQUEST,
                String.format("Invalid value for '%s'. Expected type: %s.", e.getName(), e.getRequiredType().getSimpleName())
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalExceptionResponse> globalHandler(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GlobalExceptionResponse(
                HttpStatus.BAD_REQUEST,
                String.format("Invalid request body format.")
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GlobalExceptionResponse> globalHandler(DataIntegrityViolationException e) {
        String errorMessage = "Data integrity violation occurred";

        Matcher matcher = Pattern.compile("PUBLIC\\.(\\w+).*UUID '([^']+)'").matcher(e.getMessage());
        if (matcher.find()) {
            String table = matcher.group(1).toLowerCase();
            String id = matcher.group(2);
            errorMessage = String.format("Referenced %s with ID %s does not exist", table, id);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new GlobalExceptionResponse(HttpStatus.BAD_REQUEST, errorMessage));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<GlobalExceptionResponse> globalHandler(NullPointerException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new GlobalExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Null Pointer Exception"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalExceptionResponse> globalHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new GlobalExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
