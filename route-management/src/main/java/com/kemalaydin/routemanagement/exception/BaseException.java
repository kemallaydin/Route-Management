package com.kemalaydin.routemanagement.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public abstract class BaseException extends RuntimeException {
    private final HttpStatus statusCode;

    protected BaseException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}