package vn.edu.hcmut.cse.adse.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = new Student("1", "Nguyen Van A", "vana@example.com", 20);
        student2 = new Student("2", "Tran Thi B", "thib@example.com", 21);
    }

    @Test
    void getAll_shouldReturnAllStudents() {
        when(repository.findAllByOrderByIdAsc()).thenReturn(List.of(student1, student2));

        List<Student> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Nguyen Van A", result.get(0).getName());
        assertEquals("Tran Thi B", result.get(1).getName());
        verify(repository, times(1)).findAllByOrderByIdAsc();
    }

    @Test
    void getById_whenStudentExists_shouldReturnStudent() {
        when(repository.findById("1")).thenReturn(Optional.of(student1));

        Student result = service.getById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Nguyen Van A", result.getName());
        verify(repository, times(1)).findById("1");
    }

    @Test
    void getById_whenStudentDoesNotExist_shouldReturnNull() {
        when(repository.findById("999")).thenReturn(Optional.empty());

        Student result = service.getById("999");

        assertNull(result);
        verify(repository, times(1)).findById("999");
    }

    @Test
    void searchByName_shouldReturnMatchingStudents() {
        when(repository.findByNameContainingIgnoreCase("Nguyen")).thenReturn(List.of(student1));

        List<Student> result = service.searchByName("Nguyen");

        assertEquals(1, result.size());
        assertEquals("Nguyen Van A", result.get(0).getName());
        verify(repository, times(1)).findByNameContainingIgnoreCase("Nguyen");
    }

    @Test
    void create_whenValidStudent_shouldSaveAndReturnStudent() {
        Student newStudent = new Student("3", "Le Van C", "levanc@example.com", 22);

        when(repository.existsById("3")).thenReturn(false);
        when(repository.existsByEmail("levanc@example.com")).thenReturn(false);
        when(repository.save(newStudent)).thenReturn(newStudent);

        Student result = service.create(newStudent);

        assertNotNull(result);
        assertEquals("3", result.getId());
        assertEquals("Le Van C", result.getName());
        verify(repository, times(1)).existsById("3");
        verify(repository, times(1)).existsByEmail("levanc@example.com");
        verify(repository, times(1)).save(newStudent);
    }

    @Test
    void create_whenIdAlreadyExists_shouldThrowException() {
        Student newStudent = new Student("1", "Another Student", "another@example.com", 19);

        when(repository.existsById("1")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(newStudent)
        );

        assertEquals("ID đã tồn tại", ex.getMessage());
        verify(repository, times(1)).existsById("1");
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void create_whenEmailAlreadyExists_shouldThrowException() {
        Student newStudent = new Student("3", "Le Van C", "vana@example.com", 22);

        when(repository.existsById("3")).thenReturn(false);
        when(repository.existsByEmail("vana@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.create(newStudent)
        );

        assertEquals("Email đã tồn tại", ex.getMessage());
        verify(repository, times(1)).existsById("3");
        verify(repository, times(1)).existsByEmail("vana@example.com");
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void update_whenValidData_shouldUpdateAndReturnStudent() {
        Student updatedData = new Student("1", "Nguyen Van A Updated", "vana_new@example.com", 23);

        when(repository.findById("1")).thenReturn(Optional.of(student1));
        when(repository.existsByEmail("vana_new@example.com")).thenReturn(false);
        when(repository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student result = service.update("1", updatedData);

        assertNotNull(result);
        assertEquals("1", result.getId());
        assertEquals("Nguyen Van A Updated", result.getName());
        assertEquals("vana_new@example.com", result.getEmail());
        assertEquals(23, result.getAge());

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).existsByEmail("vana_new@example.com");
        verify(repository, times(1)).save(any(Student.class));
    }

    @Test
    void update_whenStudentNotFound_shouldThrowException() {
        Student updatedData = new Student("999", "Unknown", "unknown@example.com", 20);

        when(repository.findById("999")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.update("999", updatedData)
        );

        assertEquals("Không tìm thấy sinh viên", ex.getMessage());
        verify(repository, times(1)).findById("999");
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void update_whenEmailBelongsToAnotherStudent_shouldThrowException() {
        Student updatedData = new Student("1", "Nguyen Van A", "thib@example.com", 20);

        when(repository.findById("1")).thenReturn(Optional.of(student1));
        when(repository.existsByEmail("thib@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.update("1", updatedData)
        );

        assertEquals("Email đã tồn tại", ex.getMessage());
        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).existsByEmail("thib@example.com");
        verify(repository, never()).save(any(Student.class));
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");

        service.delete("1");

        verify(repository, times(1)).existsById("1");
        verify(repository, times(1)).deleteById("1");
    }
}