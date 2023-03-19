package com.example.search.exception;

import org.springframework.http.HttpStatus;


public class ApplicationException extends RuntimeException {

    HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
    public ApplicationException() {
        super();
    }
    public ApplicationException(String message) {
        super(message);
    }
    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
