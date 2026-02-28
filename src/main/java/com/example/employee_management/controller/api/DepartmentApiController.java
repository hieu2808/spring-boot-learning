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

import com.example.employee_management.entity.Department;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.service.DepartmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
public class DepartmentApiController {

  @Autowired
  private DepartmentService departmentService;

  @GetMapping
  public ResponseEntity<List<Department>> getAllDepartments() {
    List<Department> departments = departmentService.getAllDepartments();
    return ResponseEntity.ok(departments);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
    Department department = departmentService.getDepartmentById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    return ResponseEntity.ok(department);
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<Department> getDepartmentByName(@PathVariable String name) {
    Department department = departmentService.getDepartmentByName(name)
        .orElseThrow(() -> new ResourceNotFoundException("Department", "name", name));
    return ResponseEntity.ok(department);
  }

  @PostMapping
  public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
    Department createdDepartment = departmentService.createDepartment(department);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
      @Valid @RequestBody Department department) {
    Department updatedDepartment = departmentService.updateDepartment(id, department);
    return ResponseEntity.ok(updatedDepartment);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
    departmentService.deleteDepartment(id);
    return ResponseEntity.noContent().build();
  }
}
