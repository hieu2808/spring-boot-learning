package com.example.employee_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Bật @PreAuthorize trên Controller/Service
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
    this.jwtAuthFilter = jwtAuthFilter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * 401 - Chưa xác thực: không có token hoặc token không hợp lệ
   */
  @Bean
  public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
    return (request, response, authException) -> {
      response.setStatus(401);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(
          "{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}"
      );
    };
  }

  /**
   * 403 - Đã xác thực nhưng không đủ quyền
   */
  @Bean
  public AccessDeniedHandler apiAccessDeniedHandler() {
    return (request, response, accessDeniedException) -> {
      response.setStatus(403);
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding("UTF-8");
      response.getWriter().write(
          "{\"error\": \"Forbidden\", \"message\": \"Bạn không có quyền truy cập tài nguyên này\"}"
      );
    };
  }

  /**
   * Chain 1 (ưu tiên cao hơn): Áp dụng cho /api/**
   * - Stateless (không dùng session)
   * - Xác thực bằng JWT qua JwtAuthFilter
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
            // Statistics → chỉ ADMIN (đặt trước rule GET employees để được ưu tiên)
            .requestMatchers(HttpMethod.GET, "/api/employees/statistics/**").hasRole("ADMIN")
            // GET employees/departments → USER và ADMIN (chỉ xem danh sách)
            .requestMatchers(HttpMethod.GET, "/api/employees/**", "/api/departments/**").hasAnyRole("USER", "ADMIN")
            // POST, PUT, DELETE và tất cả còn lại → chỉ ADMIN
            .anyRequest().hasRole("ADMIN")
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(apiAuthenticationEntryPoint())
            .accessDeniedHandler(apiAccessDeniedHandler())
        )
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  /**
   * Chain 2: Áp dụng cho Web (form login/session)
   * - USER chỉ được /employees/list
   * - Còn lại yêu cầu ADMIN
   */
  @Bean
  @Order(2)
  public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/register", "/css/**", "/js/**", "/actuator/**").permitAll()
            .requestMatchers("/employees/list").hasAnyRole("USER", "ADMIN")
            .anyRequest().hasRole("ADMIN")
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/employees/list", true)
            .failureUrl("/login?error=true")
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

    return http.build();
  }
}
