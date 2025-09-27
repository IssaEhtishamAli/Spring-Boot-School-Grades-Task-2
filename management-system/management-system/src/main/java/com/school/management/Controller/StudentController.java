package com.school.management.Controller;

import com.school.management.Dto.StudentDTO;
import com.school.management.Services.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentDTO dto) {
        return ResponseEntity.ok(service.addStudent(dto));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable Long id, @RequestBody StudentDTO dto) {
        return ResponseEntity.ok(service.updateStudent(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteStudent(id));
    }
    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllStudents());
    }
    @GetMapping("/{id}/grades")
    public ResponseEntity<?> getStudentGrades(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStudentProgress(id));
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getStudentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getStudentsPaginated(page, size));
    }

}

