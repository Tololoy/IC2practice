package com.example.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LibroNoEncontradoException.class)
    public ResponseEntity<Map<String,Object>> handleLibroNoEncontrado(LibroNoEncontradoException ex){
        Map<String,Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("codigo", HttpStatus.NOT_FOUND.value());
        body.put("mensaje",ex.getMessage());

        return new ResponseEntity<>(body,HttpStatus.NOT_FOUND);
    }
    public ResponseEntity<Map<String,String>> handleValidationErrors(MethodArgumentNotValidException ex){
        Map<String,String> errores = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                errores.put(err.getField(),err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errores);
    }
}
