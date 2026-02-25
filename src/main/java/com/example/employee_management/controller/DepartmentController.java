package com.example.employee_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.employee_management.entity.Department;
import com.example.employee_management.service.DepartmentService;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

  @Autowired
  private DepartmentService departmentService;

  @GetMapping("/list")
  public String listDepartments(Model model) {
    List<Department> departments = departmentService.getAllDepartments();
    model.addAttribute("departments", departments);
    return "department/list";
  }

  @GetMapping("/add")
  public String showAddForm(Model model) {
    model.addAttribute("department", new Department());
    return "department/add";
  }

  @PostMapping("/add")
  public String addDepartment(@ModelAttribute Department department) {
    departmentService.createDepartment(department);
    return "redirect:/departments/list";
  }
}
