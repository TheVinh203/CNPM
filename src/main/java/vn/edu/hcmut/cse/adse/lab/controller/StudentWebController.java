package vn.edu.hcmut.cse.adse.lab.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.service.StudentService;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentWebController {

    private final StudentService service;

    public StudentWebController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public String getAllStudents(@RequestParam(required = false) String keyword, Model model) {
        List<Student> students;

        if (keyword != null && !keyword.trim().isEmpty()) {
            students = service.searchByName(keyword);
        } else {
            students = service.getAll();
        }

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        return "students";
    }

    @GetMapping("/{id}")
    public String getStudentDetail(@PathVariable String id, Model model) {
        model.addAttribute("student", service.getById(id));
        return "student-detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "student-form";
    }

    @PostMapping
    public String createStudent(@Valid @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "student-form";
        }

        try {
            service.create(student);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("isEdit", false);
            model.addAttribute("globalError", ex.getMessage());
            return "student-form";
        }

        return "redirect:/students";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable String id, Model model) {
        Student student = service.getById(id);

        if (student == null) {
            return "redirect:/students";
        }

        model.addAttribute("student", student);
        model.addAttribute("isEdit", true);
        return "student-form";
    }

    @PostMapping("/{id}")
    public String updateStudent(@PathVariable String id,
                                @Valid @ModelAttribute("student") Student student,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "student-form";
        }

        try {
            service.update(id, student);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("isEdit", true);
            model.addAttribute("globalError", ex.getMessage());
            return "student-form";
        }

        return "redirect:/students";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable String id) {
        try {
            service.delete(id);
        } catch (IllegalArgumentException ex) {
            return "redirect:/students";
        }
        return "redirect:/students";
    }
}