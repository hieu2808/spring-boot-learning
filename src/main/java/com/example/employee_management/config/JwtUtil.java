package com.example.employee_management.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration-ms}")
  private long expirationMs;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes());
  }

  /** Tạo JWT token từ username và role */
  public String generateToken(String username, String role) {
    return Jwts.builder()
        .subject(username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expirationMs))
        .signWith(getSigningKey())
        .compact();
  }

  /** Lấy username từ token */
  public String extractUsername(String token) {
    return getClaims(token).getSubject();
  }

  /** Lấy role từ token */
  public String extractRole(String token) {
    return getClaims(token).get("role", String.class);
  }

  /** Kiểm tra token còn hợp lệ không */
  public boolean isTokenValid(String token) {
    try {
      return !getClaims(token).getExpiration().before(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
