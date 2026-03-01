# Student Management System

Ứng dụng quản lý hồ sơ sinh viên được xây dựng bằng **Spring Boot**, **Thymeleaf**, **Spring Data JPA** và **PostgreSQL**.

## 1. Giới thiệu

Hệ thống cung cấp:

- Quản lý thông tin sinh viên:
  - ID
  - Họ tên
  - Email
  - Tuổi
- REST API cho frontend hoặc ứng dụng khác
- Giao diện web SSR với Thymeleaf
- Lưu trữ dữ liệu bền vững bằng PostgreSQL

## 2. Công nghệ sử dụng

- Java 17
- Spring Boot 4.0.2
- Spring Web
- Spring Data JPA
- Thymeleaf
- Bean Validation
- PostgreSQL
- Maven Wrapper
- Docker
- Render (deploy app)
- Neon (deploy database)

## 3. Cấu trúc project

```text
src/main/java/vn/edu/hcmut/cse/adse/lab/
├── StudentManagementApplication.java
├── controller/
├── dto/
├── entity/
├── exception/
├── repository/
└── service/

src/main/resources/
├── application.properties
├── static/
│   ├── css/
│   └── js/
└── templates/
    ├── students.html
    ├── student-detail.html
    └── student-form.html