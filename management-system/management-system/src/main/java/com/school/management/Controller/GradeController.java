package com.school.management.Controller;

import com.school.management.Dto.GradeDTO;
import com.school.management.Services.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    private final GradeService service;

    public GradeController(GradeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> record(@RequestBody GradeDTO dto) {
        return ResponseEntity.ok(service.recordGrade(dto));
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<?> getByEnrollment(@PathVariable Long enrollmentId) {
        return ResponseEntity.ok(service.getGradesByEnrollment(enrollmentId));
    }
}

