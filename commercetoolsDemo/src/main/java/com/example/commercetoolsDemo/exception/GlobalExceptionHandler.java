package com.example.commercetoolsDemo.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientInventoryException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientInventory(
            InsufficientInventoryException ex) {
        log.error("Insufficient inventory: {}", ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("error", "INSUFFICIENT_INVENTORY");
        error.put("message", ex.getMessage());
        error.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InventoryServiceException.class)
    public ResponseEntity<Map<String, Object>> handleInventoryServiceException(
            InventoryServiceException ex) {
        log.error("Inventory service error: {}", ex.getMessage(), ex);

        Map<String, Object> error = new HashMap<>();
        error.put("error", "INVENTORY_SERVICE_ERROR");
        error.put("message", "Failed to communicate with inventory service");
        error.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Map<String, Object>> handleFeignException(FeignException ex) {
        log.error("Feign client error: status={}, message={}", ex.status(), ex.getMessage());

        Map<String, Object> error = new HashMap<>();
        error.put("error", "EXTERNAL_SERVICE_ERROR");
        error.put("message", "Error communicating with external service");
        error.put("status", ex.status());
        error.put("timestamp", System.currentTimeMillis());

        HttpStatus status = ex.status() >= 500
                ? HttpStatus.SERVICE_UNAVAILABLE
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);

        Map<String, Object> error = new HashMap<>();
        error.put("error", "INTERNAL_ERROR");
        error.put("message", ex.getMessage());
        error.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // ========= CUSTOM EXCEPTION CLASSES =========

    public static class InsufficientInventoryException extends RuntimeException {
        public InsufficientInventoryException(String message) {
            super(message);
        }
    }

    public static class InventoryServiceException extends RuntimeException {
        public InventoryServiceException(String message) {
            super(message);
        }

        public InventoryServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
