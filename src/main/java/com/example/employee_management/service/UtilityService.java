package com.example.employee_management.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * Bean được Spring quản lý thông qua annotation @Service (kế thừa từ @Component).
 * Cung cấp các tiện ích:
 *  1. Tự động sinh mã nhân viên (Employee Code)
 *  2. Format chuỗi họ tên chuẩn (capitalize từng từ)
 *  3. Format timestamp hiển thị
 */
@Service
public class UtilityService {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    // ===== 1. Sinh mã nhân viên tự động =====

    /**
     * Sinh mã nhân viên theo định dạng: EMP-YYYYMMDD-XXXX
     * Ví dụ: EMP-20260302-A3F9
     *
     * @return mã nhân viên duy nhất
     */
    public String generateEmployeeCode() {
        String datePart = LocalDateTime.now().format(DATE_FORMATTER);
        String randomPart = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 4)
                .toUpperCase();
        return "EMP-" + datePart + "-" + randomPart;
    }

    // ===== 2. Format chuỗi họ tên =====

    /**
     * Viết hoa chữ cái đầu của mỗi từ trong chuỗi.
     * Ví dụ: "nguyen van a" → "Nguyen Van A"
     *
     * @param input chuỗi đầu vào
     * @return chuỗi đã được format
     */
    public String capitalizeName(String input) {
        if (input == null || input.isBlank()) {
            return input;
        }
        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1))
                  .append(" ");
            }
        }
        return sb.toString().trim();
    }

    // ===== 3. Format timestamp =====

    /**
     * Trả về timestamp hiện tại theo định dạng dd/MM/yyyy HH:mm:ss
     *
     * @return chuỗi timestamp đã format
     */
    public String getCurrentTimestamp() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
