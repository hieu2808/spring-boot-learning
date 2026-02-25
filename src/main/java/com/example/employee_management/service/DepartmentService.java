package com.example.employee_management.service;

import com.example.employee_management.entity.Department;
import com.example.employee_management.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

  @Autowired
  private DepartmentRepository departmentRepository;

  public List<Department> getAllDepartments() {
    return departmentRepository.findAll();
  }

  public Optional<Department> getDepartmentById(Long id) {
    return departmentRepository.findById(id);
  }

  public Optional<Department> getDepartmentByName(String name) {
    return departmentRepository.findByName(name);
  }

  public Department createDepartment(Department department) {
    if (departmentRepository.existsByName(department.getName())) {
      throw new RuntimeException("Department with name '" + department.getName() + "' already exists");
    }
    return departmentRepository.save(department);
  }

  public Department updateDepartment(Long id, Department departmentDetails) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

    department.setName(departmentDetails.getName());
    department.setDescription(departmentDetails.getDescription());

    return departmentRepository.save(department);
  }

  public void deleteDepartment(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
    departmentRepository.delete(department);
  }
}
