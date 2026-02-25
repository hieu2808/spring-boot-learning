package com.example.employee_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.service.DepartmentService;
import com.example.employee_management.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private DepartmentService departmentService;

  @GetMapping("/list")
  public String listEmployees(Model model) {
    List<Employee> employees = employeeService.getAllEmployees();
    model.addAttribute("employees", employees);
    return "employee/list";
  }

  @GetMapping("/add")
  public String showAddForm(Model model) {
    model.addAttribute("employee", new Employee());
    model.addAttribute("departments", departmentService.getAllDepartments());
    return "employee/add";
  }

  @PostMapping("/add")
  public String addEmployee(@ModelAttribute Employee employee) {
    if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
      Department department = departmentService.getDepartmentById(employee.getDepartment().getId())
          .orElse(null);
      employee.setDepartment(department);
    }
    employeeService.createEmployee(employee);
    return "redirect:/employees/list";
  }

  @GetMapping("/search")
  public String showSearchForm(Model model) {
    model.addAttribute("departments", departmentService.getAllDepartments());
    return "employee/search";
  }

  @PostMapping("/search")
  public String searchEmployees(@RequestParam(required = false) String name,
                                @RequestParam(required = false) Long departmentId,
                                Model model) {
    List<Employee> employees;
    if (name != null && !name.isEmpty()) {
      employees = employeeService.getAllEmployees().stream()
          .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
          .toList();
    } else if (departmentId != null) {
      employees = employeeService.getEmployeesByDepartmentId(departmentId);
    } else {
      employees = employeeService.getAllEmployees();
    }
    model.addAttribute("employees", employees);
    model.addAttribute("departments", departmentService.getAllDepartments());
    model.addAttribute("name", name);
    model.addAttribute("departmentId", departmentId);
    return "employee/search";
  }
}
