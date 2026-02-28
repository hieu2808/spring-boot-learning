package com.example.employee_management.exception;

public class DuplicateResourceException extends RuntimeException {

  public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s đã tồn tại với %s: '%s'", resourceName, fieldName, fieldValue));
  }
}
