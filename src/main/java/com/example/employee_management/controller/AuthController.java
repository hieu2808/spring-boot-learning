package com.example.employee_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.service.UserService;

@Controller
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  // ===== ĐĂNG NHẬP =====
  // Spring Security tự xử lý POST /login — chỉ cần render trang

  @GetMapping("/login")
  public String showLoginForm() {
    return "auth/login";
  }

  // ===== ĐĂNG KÝ =====

  @GetMapping("/register")
  public String showRegisterForm() {
    return "auth/register";
  }

  @PostMapping("/register")
  public String register(@RequestParam String username,
                         @RequestParam String password,
                         @RequestParam String confirmPassword,
                         RedirectAttributes redirectAttributes) {
    if (!password.equals(confirmPassword)) {
      redirectAttributes.addFlashAttribute("error", "Mật khẩu xác nhận không khớp");
      return "redirect:/register";
    }

    try {
      userService.register(username, password);
      redirectAttributes.addFlashAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
      return "redirect:/login";
    } catch (DuplicateResourceException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/register";
    }
  }
}
