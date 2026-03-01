package vn.edu.hcmut.cse.adse.lab.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import vn.edu.hcmut.cse.adse.lab.entity.Student;
import vn.edu.hcmut.cse.adse.lab.service.StudentService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService service;

    @Test
    void getAllStudents_shouldReturnStudentList() throws Exception {
        List<Student> students = List.of(
                new Student("1", "Nguyen Van A", "vana@example.com", 20),
                new Student("2", "Tran Thi B", "thib@example.com", 21)
        );

        when(service.getAll()).thenReturn(students);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Nguyen Van A"))
                .andExpect(jsonPath("$[0].email").value("vana@example.com"))
                .andExpect(jsonPath("$[0].age").value(20))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].name").value("Tran Thi B"));
    }

    @Test
    void getStudentById_whenStudentExists_shouldReturnStudent() throws Exception {
        Student student = new Student("1", "Nguyen Van A", "vana@example.com", 20);

        when(service.getById("1")).thenReturn(student);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Nguyen Van A"))
                .andExpect(jsonPath("$.email").value("vana@example.com"))
                .andExpect(jsonPath("$.age").value(20));
    }

    @Test
    void getStudentById_whenStudentDoesNotExist_shouldReturnEmptyBody() throws Exception {
        when(service.getById("999")).thenReturn(null);

        mockMvc.perform(get("/api/students/999"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void createStudent_whenValidRequest_shouldReturnCreatedStudent() throws Exception {
        Student response = new Student("3", "Le Van C", "levanc@example.com", 22);

        when(service.create(any(Student.class))).thenReturn(response);

        String json = """
                {
                  "id": "3",
                  "name": "Le Van C",
                  "email": "levanc@example.com",
                  "age": 22
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Le Van C"))
                .andExpect(jsonPath("$.email").value("levanc@example.com"))
                .andExpect(jsonPath("$.age").value(22));
    }

    @Test
    void createStudent_whenInvalidRequest_shouldReturnBadRequest() throws Exception {
        String json = """
                {
                  "id": "",
                  "name": "",
                  "email": "not-an-email",
                  "age": 0
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void updateStudent_whenValidRequest_shouldReturnUpdatedStudent() throws Exception {
        Student response = new Student("1", "Nguyen Van A Updated", "vana_new@example.com", 23);

        when(service.update(eq("1"), any(Student.class))).thenReturn(response);

        String json = """
                {
                  "id": "1",
                  "name": "Nguyen Van A Updated",
                  "email": "vana_new@example.com",
                  "age": 23
                }
                """;

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Nguyen Van A Updated"))
                .andExpect(jsonPath("$.email").value("vana_new@example.com"))
                .andExpect(jsonPath("$.age").value(23));
    }

    @Test
    void deleteStudent_shouldReturnNoContent() throws Exception {
        doNothing().when(service).delete("1");

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}