package com.example.employee_management.repository;

import com.example.employee_management.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  Optional<Department> findByName(String name);

  boolean existsByName(String name);
}
