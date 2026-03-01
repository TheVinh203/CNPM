package vn.edu.hcmut.cse.adse.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hcmut.cse.adse.lab.entity.Student;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByNameContainingIgnoreCase(String keyword);

    boolean existsByEmail(String email);

    List<Student> findAllByOrderByIdAsc();
}