package vn.edu.hcmut.cse.adse.lab.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmut.cse.adse.lab.dto.StudentRequest;
import vn.edu.hcmut.cse.adse.lab.dto.StudentResponse;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public List<StudentResponse> getAllStudents() {
        return service.getAll()
                .stream()
                .map(StudentResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String id) {
        Student student = service.getById(id);
        return ResponseEntity.ok(StudentResponse.fromEntity(student));
    }

    @PostMapping
    public ResponseEntity<StudentResponse> createStudent(@Valid @RequestBody StudentRequest request) {
        Student student = new Student(
                request.getId(),
                request.getName(),
                request.getEmail(),
                request.getAge()
        );

        Student createdStudent = service.create(student);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(StudentResponse.fromEntity(createdStudent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable String id,
                                                         @Valid @RequestBody StudentRequest request) {
        Student student = new Student(
                request.getId(),
                request.getName(),
                request.getEmail(),
                request.getAge()
        );

        Student updatedStudent = service.update(id, student);
        return ResponseEntity.ok(StudentResponse.fromEntity(updatedStudent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}