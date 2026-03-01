package vn.edu.hcmut.cse.adse.lab.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class StudentRequest {

    @NotBlank(message = "ID không được để trống")
    private String id;

    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @Min(value = 1, message = "Tuổi phải lớn hơn 0")
    private int age;

    public StudentRequest() {
    }

    public StudentRequest(String id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NotBlank(message = "Tên không được để trống") String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotBlank(message = "Email không được để trống") @Email(message = "Email không hợp lệ") String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public @Min(value = 1, message = "Tuổi phải lớn hơn 0") int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}