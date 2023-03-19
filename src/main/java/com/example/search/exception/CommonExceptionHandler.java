package com.example.search.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({ ApplicationException.class })
    public ResponseEntity<?> handle(Exception exception) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", exception.getMessage());

        ApplicationException applicationException = (ApplicationException)exception;
        ResponseEntity responseEntity = ResponseEntity.status(applicationException.statusCode).body(hashMap);

        return responseEntity;
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<?> illegalArgumentHandle(Exception exception) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("message", exception.getMessage());

        ResponseEntity responseEntity = ResponseEntity.badRequest().body(hashMap);

        return responseEntity;
    }
}
