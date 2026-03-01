package com.example.employee_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Username không được để trống")
  @Size(min = 3, max = 50, message = "Username phải từ 3 đến 50 ký tự")
  @Column(nullable = false, unique = true, length = 50)
  private String username;

  @NotBlank(message = "Password không được để trống")
  @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
  @Column(nullable = false)
  private String password;

  @NotNull(message = "Role không được để trống")
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Role role;

  // Constructors
  public User() {
  }

  public User(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", username='" + username + '\'' +
        ", role=" + role +
        '}';
  }
}
