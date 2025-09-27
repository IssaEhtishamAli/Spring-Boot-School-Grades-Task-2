# Student-Management RESTFull API

## Project Description
This is a RESTful API built using Spring Boot for managing **students, courses, enrollments, and grades**.  
It supports:  
- CRUD operations for students and courses  
- Enrolling students in multiple courses with pre-requisite and capacity validation  
- Recording and updating grades per enrollment  
- GPA computation per student  
- Pagination, validation, and proper error handling  

---

## Environment Setup

### Prerequisites
- Java 21
- Spring Boot 3.5.6  
- Maven  
- SQL Server (local or remote)  
- IDE (IntelliJ IDEA recommended)  

## Endpoints
## Students
| **Method** | **Endpoint**                  | **Description**                                        | **Payload Example**                                                                                                                                                                            |
| ---------- | ----------------------------- | ------------------------------------------------------ | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **POST**   | `/api/students`               | Create a student                                       | <pre>{<br> "firstName":"Ehtisham",<br> "lastName":"Ali",<br> "email":"[ehte@example.com](mailto:ehte@example.com)",<br> "dateOfBirth":"1997-10-20",<br> "phoneNumber":"03485153893"<br>}</pre> |
| **GET**    | `/api/students`               | List students (pagination supported `?page=0&size=10`) | -                                                                                                                                                                                              |
| **PUT**    | `/api/students/{id}`          | Update student                                         | Same as POST  + Id                                                                                                                                                                                 |
| **DELETE** | `/api/students/{id}`          | Delete student                                         | -                                                                                                                                                                                              |
| **GET**    | `/api/students/{id}/progress` | Get student academic progress & GPA                    | -                                                                                                                                                                                              |
## Courses
| **Method** | **Endpoint**        | **Description**                     | **Payload Example**                                                                                                       |
| ---------- | ------------------- | ----------------------------------- | ------------------------------------------------------------------------------------------------------------------------- |
| **POST**   | `/api/courses`      | Create course                       | <pre>{<br> "courseName":"Math",<br> "courseCode":"MATH101",<br> "capacity":30,<br> "preRequisiteCourseId":null<br>}</pre> |
| **GET**    | `/api/courses`      | List courses (pagination supported) | -                                                                                                                         |
| **PUT**    | `/api/courses/{id}` | Update course                       | Same as POST   + Id                                                                                                          |
| **DELETE** | `/api/courses/{id}` | Delete course                       | -                                                                                                                         |

## Enrollments
| **Method** | **Endpoint**            | **Description**                                     | **Payload Example**                                                      |
| ---------- | ----------------------- | --------------------------------------------------- | ------------------------------------------------------------------------ |
| **POST**   | `/api/enrollments`      | Enroll student (validate pre-requisites & capacity) | <pre>{<br> "studentId":1,<br> "courseId":2,<br> "active":true<br>}</pre> |

## 🏆 Grades
| **Method** | **Endpoint**                            | **Description**         | **Payload Example**                                                                                          |
| ---------- | --------------------------------------- | ----------------------- | ------------------------------------------------------------------------------------------------------------ |
| **POST**   | `/api/grades`                           | Record grade            | <pre>{<br> "enrollmentId":1,<br> "gradeType":"Final",<br> "gradeValue":"A",<br> "finalized":false<br>}</pre> |
| **GET**    | `/api/grades/enrollment/{enrollmentId}` | Fetch grades per course | -  

## Swagger UI


<img width="2992" height="3512" alt="localhost_8087_swagger-ui_index html" src="https://github.com/user-attachments/assets/dd52f814-e34e-4a11-8d9c-c78c8dd9185a" />

