package com.school.management.Controller;

import com.school.management.Dto.EnrollmentDTO;
import com.school.management.Services.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService service;

    public EnrollmentController(EnrollmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> enroll(@RequestBody EnrollmentDTO dto) {
        return ResponseEntity.ok(service.enrollStudent(dto));
    }
}

