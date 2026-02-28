package com.example.employee_management.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(String.format("%s không tìm thấy với %s: '%s'", resourceName, fieldName, fieldValue));
  }
}
