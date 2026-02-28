package com.example.employee_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.dto.EmployeeStatisticsDTO;
import com.example.employee_management.entity.Department;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.service.DepartmentService;
import com.example.employee_management.service.EmployeeService;

import jakarta.validation.Valid;

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
  public String addEmployee(@Valid @ModelAttribute Employee employee,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    // Nếu validation fail → quay lại form, hiện lỗi
    if (bindingResult.hasErrors()) {
      model.addAttribute("departments", departmentService.getAllDepartments());
      return "employee/add";
    }

    // Gán department từ DB
    if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
      Department department = departmentService.getDepartmentById(employee.getDepartment().getId())
          .orElse(null);
      employee.setDepartment(department);
    }

    try {
      employeeService.createEmployee(employee);
      redirectAttributes.addFlashAttribute("successMessage", "Thêm nhân viên thành công!");
      return "redirect:/employees/list";
    } catch (DuplicateResourceException ex) {
      bindingResult.rejectValue("email", "duplicate", ex.getMessage());
      model.addAttribute("departments", departmentService.getAllDepartments());
      return "employee/add";
    }
  }

  @GetMapping("/search")
  public String showSearchForm(Model model) {
    model.addAttribute("departments", departmentService.getAllDepartments());
    return "employee/search";
  }

  @GetMapping("/statistics")
  public String showStatistics(Model model) {
    EmployeeStatisticsDTO statistics = employeeService.getFullStatistics();
    model.addAttribute("stats", statistics);
    return "employee/statistics";
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
