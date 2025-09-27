package com.school.management.Controller;

import com.school.management.Dto.CourseDTO;
import com.school.management.Services.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseDTO dto) {
        return ResponseEntity.ok(service.addCourse(dto));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAllCourses());
    }
    @GetMapping("/paginated")
    public ResponseEntity<?> getStudentsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(service.getCoursesPaginated(page, size));
    }
    // Update course
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCourse(
            @PathVariable Long id, @RequestBody CourseDTO dto) {
        return ResponseEntity.ok(service.updateCourse(id, dto));
    }

    // Delete course
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteCourse(id));
    }
}
