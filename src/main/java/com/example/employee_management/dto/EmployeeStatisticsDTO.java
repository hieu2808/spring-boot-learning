package com.example.employee_management.dto;

import java.util.List;

public class EmployeeStatisticsDTO {

  private long totalEmployees;
  private long totalDepartments;
  private long departmentsWithEmployees;
  private String departmentWithMostEmployees;
  private long mostEmployeesCount;
  private String departmentWithLeastEmployees;
  private long leastEmployeesCount;
  private List<DepartmentStatDTO> departmentStats;

  public EmployeeStatisticsDTO() {
  }

  // Getters and Setters
  public long getTotalEmployees() {
    return totalEmployees;
  }

  public void setTotalEmployees(long totalEmployees) {
    this.totalEmployees = totalEmployees;
  }

  public long getTotalDepartments() {
    return totalDepartments;
  }

  public void setTotalDepartments(long totalDepartments) {
    this.totalDepartments = totalDepartments;
  }

  public long getDepartmentsWithEmployees() {
    return departmentsWithEmployees;
  }

  public void setDepartmentsWithEmployees(long departmentsWithEmployees) {
    this.departmentsWithEmployees = departmentsWithEmployees;
  }

  public String getDepartmentWithMostEmployees() {
    return departmentWithMostEmployees;
  }

  public void setDepartmentWithMostEmployees(String departmentWithMostEmployees) {
    this.departmentWithMostEmployees = departmentWithMostEmployees;
  }

  public long getMostEmployeesCount() {
    return mostEmployeesCount;
  }

  public void setMostEmployeesCount(long mostEmployeesCount) {
    this.mostEmployeesCount = mostEmployeesCount;
  }

  public String getDepartmentWithLeastEmployees() {
    return departmentWithLeastEmployees;
  }

  public void setDepartmentWithLeastEmployees(String departmentWithLeastEmployees) {
    this.departmentWithLeastEmployees = departmentWithLeastEmployees;
  }

  public long getLeastEmployeesCount() {
    return leastEmployeesCount;
  }

  public void setLeastEmployeesCount(long leastEmployeesCount) {
    this.leastEmployeesCount = leastEmployeesCount;
  }

  public List<DepartmentStatDTO> getDepartmentStats() {
    return departmentStats;
  }

  public void setDepartmentStats(List<DepartmentStatDTO> departmentStats) {
    this.departmentStats = departmentStats;
  }
}
