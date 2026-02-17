package com.example.employee_management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // Đánh dấu đây là nơi tiếp nhận Request
public class HelloController {

    @GetMapping("/hello") // Khi gọi địa chỉ /hello, hàm này sẽ chạy
    public String sayHello() {
        return "Chào mừng bạn đến với Employee Management System!";
    }
}
