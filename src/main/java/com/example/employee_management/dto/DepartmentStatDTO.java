package com.example.employee_management.dto;

public class DepartmentStatDTO {

  private String departmentName;
  private long employeeCount;

  public DepartmentStatDTO() {
  }

  public DepartmentStatDTO(String departmentName, long employeeCount) {
    this.departmentName = departmentName;
    this.employeeCount = employeeCount;
  }

  public String getDepartmentName() {
    return departmentName;
  }

  public void setDepartmentName(String departmentName) {
    this.departmentName = departmentName;
  }

  public long getEmployeeCount() {
    return employeeCount;
  }

  public void setEmployeeCount(long employeeCount) {
    this.employeeCount = employeeCount;
  }
}
