package com.example.employee_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.employee_management.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Optional<Employee> findByEmail(String email);

  List<Employee> findByDepartmentId(Long departmentId);

  boolean existsByEmail(String email);

  long countByDepartmentId(Long departmentId);

  // @Query: Đếm tổng số nhân viên
  @Query("SELECT COUNT(e) FROM Employee e")
  long countTotalEmployees();

  // @Query: Thống kê số lượng nhân viên theo từng phòng ban
  @Query("SELECT e.department.name, COUNT(e) FROM Employee e GROUP BY e.department.name ORDER BY COUNT(e) DESC")
  List<Object[]> countEmployeesByDepartment();

  // @Query: Tìm phòng ban có nhiều nhân viên nhất (bao gồm cả PB có 0 NV)
  @Query("SELECT d.name, COUNT(e) as cnt FROM Department d LEFT JOIN d.employees e GROUP BY d.name ORDER BY cnt DESC LIMIT 1")
  List<Object[]> findDepartmentWithMostEmployees();

  // @Query: Tìm phòng ban có ít nhân viên nhất (bao gồm cả PB có 0 NV)
  @Query("SELECT d.name, COUNT(e) as cnt FROM Department d LEFT JOIN d.employees e GROUP BY d.name ORDER BY cnt ASC LIMIT 1")
  List<Object[]> findDepartmentWithLeastEmployees();

  // @Query: Đếm số phòng ban có nhân viên
  @Query("SELECT COUNT(DISTINCT e.department) FROM Employee e")
  long countDepartmentsWithEmployees();
}
