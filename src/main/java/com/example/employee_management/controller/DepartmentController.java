package com.example.employee_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.employee_management.entity.Department;
import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.service.DepartmentService;

import jakarta.validation.Valid;

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
  public String addDepartment(@Valid @ModelAttribute Department department,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    // Nếu validation fail → quay lại form, hiện lỗi
    if (bindingResult.hasErrors()) {
      return "department/add";
    }

    try {
      departmentService.createDepartment(department);
      redirectAttributes.addFlashAttribute("successMessage", "Thêm phòng ban thành công!");
      return "redirect:/departments/list";
    } catch (DuplicateResourceException ex) {
      bindingResult.rejectValue("name", "duplicate", ex.getMessage());
      return "department/add";
    }
  }

  // ==================== Edit ====================
  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    Department department = departmentService.getDepartmentById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", "id", id));
    model.addAttribute("department", department);
    return "department/edit";
  }

  @PostMapping("/edit/{id}")
  public String updateDepartment(@PathVariable Long id,
      @Valid @ModelAttribute Department department,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      department.setId(id);
      return "department/edit";
    }

    try {
      departmentService.updateDepartment(id, department);
      redirectAttributes.addFlashAttribute("successMessage", "Cập nhật phòng ban thành công!");
      return "redirect:/departments/list";
    } catch (DuplicateResourceException ex) {
      bindingResult.rejectValue("name", "duplicate", ex.getMessage());
      department.setId(id);
      return "department/edit";
    }
  }

  // ==================== Delete ====================
  @GetMapping("/delete/{id}")
  public String deleteDepartment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
      departmentService.deleteDepartment(id);
      redirectAttributes.addFlashAttribute("successMessage", "Xóa phòng ban thành công!");
    } catch (ResourceNotFoundException ex) {
      redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
    } catch (Exception ex) {
      redirectAttributes.addFlashAttribute("errorMessage",
          "Không thể xóa phòng ban vì vẫn còn nhân viên thuộc phòng ban này!");
    }
    return "redirect:/departments/list";
  }
}
