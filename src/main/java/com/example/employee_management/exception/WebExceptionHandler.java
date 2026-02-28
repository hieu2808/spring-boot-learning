package com.example.employee_management.exception;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.controller.DepartmentController;
import com.example.employee_management.controller.EmployeeController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * Xử lý exception cho Web (Thymeleaf) controllers.
 * Order(1): ưu tiên cao hơn GlobalExceptionHandler
 */
@Slf4j
@Order(1)
@ControllerAdvice(assignableTypes = { DepartmentController.class, EmployeeController.class })
public class WebExceptionHandler {

  // ==================== Resource Not Found → redirect về list ====================
  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFound(ResourceNotFoundException ex,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    log.error("Resource not found: {}", ex.getMessage());
    redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

    // Redirect về trang list phù hợp
    String uri = request.getRequestURI();
    if (uri.contains("/departments")) {
      return "redirect:/departments/list";
    } else if (uri.contains("/employees")) {
      return "redirect:/employees/list";
    }
    return "redirect:/";
  }

  // ==================== Duplicate Resource → redirect về list ====================
  @ExceptionHandler(DuplicateResourceException.class)
  public String handleDuplicateResource(DuplicateResourceException ex,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    log.warn("Duplicate resource: {}", ex.getMessage());
    redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

    String uri = request.getRequestURI();
    if (uri.contains("/departments")) {
      return "redirect:/departments/list";
    } else if (uri.contains("/employees")) {
      return "redirect:/employees/list";
    }
    return "redirect:/";
  }

  // ==================== Fallback — Unexpected Errors ====================
  @ExceptionHandler(Exception.class)
  public String handleGenericException(Exception ex,
      HttpServletRequest request,
      RedirectAttributes redirectAttributes) {
    log.error("Unexpected error on web page: ", ex);
    redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi không mong muốn");

    String uri = request.getRequestURI();
    if (uri.contains("/departments")) {
      return "redirect:/departments/list";
    } else if (uri.contains("/employees")) {
      return "redirect:/employees/list";
    }
    return "redirect:/";
  }
}
