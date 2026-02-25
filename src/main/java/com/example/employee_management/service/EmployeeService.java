package com.example.employee_management.service;

import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private DepartmentRepository departmentRepository;

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
