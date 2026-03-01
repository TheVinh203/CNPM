package vn.edu.hcmut.cse.adse.lab.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.repository.StudentRepository;

import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository repository;

    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Student> getAll() {
        return repository.findAllByOrderByIdAsc();
    }

    @Transactional(readOnly = true)
    public Student getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Student> searchByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAll();
        }
        return repository.findByNameContainingIgnoreCase(keyword.trim());
    }

    public Student create(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Dữ liệu sinh viên không hợp lệ");
        }

        if (repository.existsById(student.getId())) {
            throw new IllegalArgumentException("ID đã tồn tại");
        }

        if (repository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        return repository.save(student);
    }

    public Student update(String id, Student updatedStudent) {
        Student existingStudent = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sinh viên"));

        boolean emailChanged = !existingStudent.getEmail().equals(updatedStudent.getEmail());
        if (emailChanged && repository.existsByEmail(updatedStudent.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        existingStudent.setName(updatedStudent.getName());
        existingStudent.setEmail(updatedStudent.getEmail());
        existingStudent.setAge(updatedStudent.getAge());

        return repository.save(existingStudent);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Không tìm thấy sinh viên");
        }
        repository.deleteById(id);
    }
}