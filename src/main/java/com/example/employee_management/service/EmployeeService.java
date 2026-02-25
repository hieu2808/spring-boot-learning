package com.example.employee_management.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee_management.dto.DepartmentStatDTO;
import com.example.employee_management.dto.EmployeeStatisticsDTO;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

  // ===== Thống kê =====

  public long getTotalEmployeeCount() {
    return employeeRepository.countTotalEmployees();
  }

  public Map<String, Long> getEmployeeCountByDepartment() {
    Map<String, Long> stats = new LinkedHashMap<>();
    List<Object[]> results = employeeRepository.countEmployeesByDepartment();
    for (Object[] row : results) {
      String departmentName = (String) row[0];
      Long count = (Long) row[1];
      stats.put(departmentName, count);
    }
    return stats;
  }

  public EmployeeStatisticsDTO getFullStatistics() {
    EmployeeStatisticsDTO dto = new EmployeeStatisticsDTO();

    // Tổng số nhân viên (dùng @Query)
    dto.setTotalEmployees(employeeRepository.countTotalEmployees());

    // Tổng số phòng ban
    dto.setTotalDepartments(departmentRepository.count());

    // Số phòng ban có nhân viên (dùng @Query)
    dto.setDepartmentsWithEmployees(employeeRepository.countDepartmentsWithEmployees());

    // Thống kê theo phòng ban (dùng @Query)
    List<Object[]> results = employeeRepository.countEmployeesByDepartment();
    List<DepartmentStatDTO> departmentStats = new ArrayList<>();
    for (Object[] row : results) {
      departmentStats.add(new DepartmentStatDTO((String) row[0], (Long) row[1]));
    }
    dto.setDepartmentStats(departmentStats);

    // Phòng ban nhiều nhân viên nhất (dùng @Query)
    Object[] most = employeeRepository.findDepartmentWithMostEmployees();
    if (most != null && most.length == 2) {
      dto.setDepartmentWithMostEmployees((String) most[0]);
      dto.setMostEmployeesCount((Long) most[1]);
    }

    // Phòng ban ít nhân viên nhất (dùng @Query)
    Object[] least = employeeRepository.findDepartmentWithLeastEmployees();
    if (least != null && least.length == 2) {
      dto.setDepartmentWithLeastEmployees((String) least[0]);
      dto.setLeastEmployeesCount((Long) least[1]);
    }

    return dto;
  }

  public List<Employee> getAllEmployees() {
    return employeeRepository.findAll();
  }

  public Optional<Employee> getEmployeeById(Long id) {
    return employeeRepository.findById(id);
  }

  public Optional<Employee> getEmployeeByEmail(String email) {
    return employeeRepository.findByEmail(email);
  }

  public List<Employee> getEmployeesByDepartmentId(Long departmentId) {
    return employeeRepository.findByDepartmentId(departmentId);
  }

  public Employee createEmployee(Employee employee) {
    if (employeeRepository.existsByEmail(employee.getEmail())) {
      throw new RuntimeException("Employee with email '" + employee.getEmail() + "' already exists");
    }

    // Verify department exists
    if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
      Department department = departmentRepository.findById(employee.getDepartment().getId())
          .orElseThrow(() -> new RuntimeException("Department not found with id: " + employee.getDepartment().getId()));
      employee.setDepartment(department);
    }

    return employeeRepository.save(employee);
  }

  public Employee updateEmployee(Long id, Employee employeeDetails) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

    employee.setName(employeeDetails.getName());
    employee.setEmail(employeeDetails.getEmail());

    if (employeeDetails.getDepartment() != null && employeeDetails.getDepartment().getId() != null) {
      Department department = departmentRepository.findById(employeeDetails.getDepartment().getId())
          .orElseThrow(() -> new RuntimeException("Department not found with id: " + employeeDetails.getDepartment().getId()));
      employee.setDepartment(department);
    }

    return employeeRepository.save(employee);
  }

  public void deleteEmployee(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
    employeeRepository.delete(employee);
  }
}
