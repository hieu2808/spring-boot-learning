package com.example.employee_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee_management.entity.Department;
import com.example.employee_management.repository.DepartmentRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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
    log.info("Creating department with name: {}", department.getName());

    if (departmentRepository.existsByName(department.getName())) {
      log.warn("Duplicate department name detected: {}", department.getName());
      throw new RuntimeException("Department with name '" + department.getName() + "' already exists");
    }

    Department saved = departmentRepository.save(department);
    log.info("Department created successfully - id: {}, name: {}", saved.getId(), saved.getName());
    return saved;
  }

  public Department updateDepartment(Long id, Department departmentDetails) {
    log.info("Updating department with id: {}", id);

    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Department not found for update, id: {}", id);
          return new RuntimeException("Department not found with id: " + id);
        });

    log.debug("Updating department: name [{}] -> [{}]", department.getName(), departmentDetails.getName());

    department.setName(departmentDetails.getName());
    department.setDescription(departmentDetails.getDescription());

    Department updated = departmentRepository.save(department);
    log.info("Department updated successfully - id: {}, name: {}", updated.getId(), updated.getName());
    return updated;
  }

  public void deleteDepartment(Long id) {
    log.info("Deleting department with id: {}", id);

    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> {
          log.error("Department not found for deletion, id: {}", id);
          return new RuntimeException("Department not found with id: " + id);
        });

    departmentRepository.delete(department);
    log.info("Department deleted successfully - id: {}, name: {}", id, department.getName());
  }
}
