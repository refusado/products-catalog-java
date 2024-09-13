package com.refu.productscatalog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {

    // create error responses
    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        Map<String, String> errorMap = new HashMap<>();
        
        errorMap.put("error", message);

        return new ResponseEntity<>(errorMap, status);
    }

    // not found
    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<?> handleProductNotFoundException(ProductNotFound ex) {
        return createErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = "Validation error. Please check your input.";

        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    // invalid arguments with custom message
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        String message = ex.getMessage();
        if (message == null || message.isEmpty()) {
            message = "Invalid argument provided";
        }
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    // body json parsing errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String message = "Invalid request body.";
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    // uncaught errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex) {
        System.out.println(ex.getMessage());

        String message = "An unexpected error occurred";

        return createErrorResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}