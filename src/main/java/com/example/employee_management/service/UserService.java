package com.example.employee_management.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employee_management.entity.Role;
import com.example.employee_management.entity.User;
import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * Spring Security gọi method này khi xử lý đăng nhập.
   * Load user từ DB theo username, trả về UserDetails.
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.debug("Load user by username: {}", username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User không tìm thấy: " + username));

    // Trả về Spring Security UserDetails với ROLE_ prefix
    return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPassword())
        .roles(user.getRole().name())  // ADMIN → ROLE_ADMIN, USER → ROLE_USER
        .build();
  }

  /**
   * Đăng ký user mới, password được hash bằng BCrypt.
   */
  public User register(String username, String password) {
    log.debug("Đăng ký user mới: {}", username);

    if (userRepository.existsByUsername(username)) {
      throw new DuplicateResourceException("User", "username", username);
    }

    // Hash password trước khi lưu vào DB
    String hashedPassword = passwordEncoder.encode(password);
    User user = new User(username, hashedPassword, Role.USER);
    User saved = userRepository.save(user);
    log.info("Đăng ký thành công: {}", username);
    return saved;
  }
}
