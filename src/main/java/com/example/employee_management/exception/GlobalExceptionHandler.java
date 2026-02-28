package com.example.employee_management.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;

/**
 * Xử lý exception cho API controllers (package controller.api).
 * Trả về JSON, không bao giờ trả trace.
 * Order(2): ưu tiên thấp hơn WebExceptionHandler
 */
@Slf4j
@RestControllerAdvice
@org.springframework.core.annotation.Order(2)
public class GlobalExceptionHandler {

  // ==================== 1. Validation Errors (400) ====================
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.put(fieldError.getField(), fieldError.getDefaultMessage());
    }

    log.warn("Validation failed: {}", errors);

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Validation Failed")
        .message("Dữ liệu không hợp lệ")
        .validationErrors(errors)
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // ==================== 2. JSON parse error (400) ====================
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
    log.warn("JSON parse error: {}", ex.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message("Request body không đúng định dạng JSON")
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // ==================== 3. Type mismatch (400) ====================
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    log.warn("Type mismatch: {}", ex.getMessage());

    String message = String.format("Tham số '%s' có giá trị '%s' không hợp lệ", ex.getName(), ex.getValue());

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Bad Request")
        .message(message)
        .build();

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

  // ==================== 4. URL không tồn tại (404) ====================
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
    log.warn("No resource found: {}", ex.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Not Found")
        .message("Đường dẫn không tồn tại")
        .build();

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  // ==================== 5. Resource Not Found (404) ====================
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
    log.error("Resource not found: {}", ex.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.NOT_FOUND.value())
        .error("Not Found")
        .message(ex.getMessage())
        .build();

    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
  }

  // ==================== 5. Duplicate Resource (409) ====================
  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
    log.warn("Duplicate resource: {}", ex.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.CONFLICT.value())
        .error("Conflict")
        .message(ex.getMessage())
        .build();

    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  // ==================== 6. Fallback — Unexpected Errors (500) ====================
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    log.error("Unexpected error occurred: ", ex);

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error("Internal Server Error")
        .message("Đã xảy ra lỗi không mong muốn")
        .build();

    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
