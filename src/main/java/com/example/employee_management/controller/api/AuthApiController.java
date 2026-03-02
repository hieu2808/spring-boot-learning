package com.example.employee_management.controller.api;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.config.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;

  public AuthApiController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
  }

  /**
   * POST /api/auth/login
   * Body: { "username": "admin", "password": "123456" }
   * Response: { "token": "eyJ..." }
   */
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    String username = body.get("username");
    String password = body.get("password");

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password)
      );

      // Lấy role từ Authentication (bỏ prefix ROLE_)
      String role = authentication.getAuthorities().iterator().next()
          .getAuthority().replace("ROLE_", "");

      String token = jwtUtil.generateToken(username, role);
      return ResponseEntity.ok(Map.of("token", token));

    } catch (AuthenticationException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(Map.of("error", "Username hoặc mật khẩu không đúng"));
    }
  }
}
