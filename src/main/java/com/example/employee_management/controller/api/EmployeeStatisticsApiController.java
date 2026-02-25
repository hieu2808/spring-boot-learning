package com.example.employee_management.controller.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.dto.EmployeeStatisticsDTO;
import com.example.employee_management.service.EmployeeService;

@RestController
@RequestMapping("/api/employees/statistics")
public class EmployeeStatisticsApiController {

  @Autowired
  private EmployeeService employeeService;

  // GET /api/employees/statistics — Toàn bộ thống kê
  @GetMapping
  public ResponseEntity<EmployeeStatisticsDTO> getFullStatistics() {
    EmployeeStatisticsDTO statistics = employeeService.getFullStatistics();
    return ResponseEntity.ok(statistics);
  }

  // GET /api/employees/statistics/total — Tổng số nhân viên
  @GetMapping("/total")
  public ResponseEntity<Map<String, Long>> getTotalEmployees() {
    long total = employeeService.getTotalEmployeeCount();
    return ResponseEntity.ok(Map.of("totalEmployees", total));
  }

  // GET /api/employees/statistics/by-department — Số nhân viên theo phòng ban
  @GetMapping("/by-department")
  public ResponseEntity<Map<String, Long>> getEmployeeCountByDepartment() {
    Map<String, Long> stats = employeeService.getEmployeeCountByDepartment();
    return ResponseEntity.ok(stats);
  }
}
