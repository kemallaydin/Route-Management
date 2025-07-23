package com.kemalaydin.routemanagement.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        log.warn("BaseException occurred", e);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(buildErrorResponse(e.getMessage(), e.getStatusCode().value()));
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageConversionException(HttpMessageConversionException e) {
        log.warn("HttpMessageConversionException occurred", e);

        String message = "Request body is invalid or malformed";

        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("MethodArgumentTypeMismatchException occurred", e);

        String paramName = e.getName();
        String expectedType = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown";

        String message = String.format("Parameter '%s' must be of type %s", paramName, expectedType);

        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("ConstraintViolationException occurred", e);

        String message = e.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException occurred", e);

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        return ResponseEntity
                .badRequest()
                .body(buildErrorResponse(message, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) throws Exception {
        if (e instanceof AuthorizationDeniedException) {
            throw e;
        }
        log.error("Unhandled exception occurred", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    private static ErrorResponse buildErrorResponse(String message, int statusCode) {
        return ErrorResponse.builder()
                .status(statusCode)
                .message(message)
                .timestamp(Instant.now().toString())
                .traceId(MDC.get("traceId"))
                .build();
    }
}
