package com.example.employee_management.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeApiController {

  @Autowired
  private EmployeeService employeeService;

  @GetMapping
  public ResponseEntity<List<Employee>> getAllEmployees() {
    List<Employee> employees = employeeService.getAllEmployees();
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
    Employee employee = employeeService.getEmployeeById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    return ResponseEntity.ok(employee);
  }

  @GetMapping("/email/{email}")
  public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
    Employee employee = employeeService.getEmployeeByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", "email", email));
    return ResponseEntity.ok(employee);
  }

  @GetMapping("/department/{departmentId}")
  public ResponseEntity<List<Employee>> getEmployeesByDepartment(@PathVariable Long departmentId) {
    List<Employee> employees = employeeService.getEmployeesByDepartmentId(departmentId);
    return ResponseEntity.ok(employees);
  }

  @PostMapping
  public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
    Employee createdEmployee = employeeService.createEmployee(employee);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
      @Valid @RequestBody Employee employee) {
    Employee updatedEmployee = employeeService.updateEmployee(id, employee);
    return ResponseEntity.ok(updatedEmployee);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }
}
