package com.school.management.Services;

import com.school.management.Dto.GradeDTO;
import com.school.management.Models.Enrollments.mEnrollments;
import com.school.management.Models.Generic.mGeneric;
import com.school.management.Models.Grades.mGrades;
import com.school.management.Repositriy.EnrollmentRepository;
import com.school.management.Repositriy.GradeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GradeService {

    private final GradeRepository repo;
    private final EnrollmentRepository enrollmentRepo;

    public GradeService(GradeRepository repo, EnrollmentRepository enrollmentRepo) {
        this.repo = repo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public mGeneric.mAPIResponse<GradeDTO> recordGrade(GradeDTO dto) {
        // check enrollment exist
        Optional<mEnrollments> enrollmentOpt = enrollmentRepo.findById(dto.getEnrollmentId());
        if (enrollmentOpt.isEmpty()) {
            return new mGeneric.mAPIResponse<>(404, "Enrollment not found");
        }
        mEnrollments enrollment = enrollmentOpt.get();

        // check if course already completed → disallow new grades
        if (enrollment.isCompleted()) {
            return new mGeneric.mAPIResponse<>(400, "Cannot record grade. Course already completed.");
        }

        // build grade
        mGrades g = new mGrades();
        g.setEnrollment(enrollment);
        g.setGradeType(dto.getGradeType());
        g.setGradeValue(dto.getGradeValue());
        g.setFinalized(dto.isFinalized());

        // save
        mGrades saved = repo.save(g);
        dto.setId(saved.getId());

        return new mGeneric.mAPIResponse<>(201, "Grade recorded", dto);
    }

    public mGeneric.mAPIResponse<List<GradeDTO>> getGradesByEnrollment(Long enrollmentId) {
        // check enrollment exist
        if (!enrollmentRepo.existsById(enrollmentId)) {
            return new mGeneric.mAPIResponse<>(404, "Enrollment not found");
        }

        List<GradeDTO> grades = repo.findByEnrollmentId(enrollmentId).stream()
                .map(g -> new GradeDTO(
                        g.getId(),
                        g.getGradeType(),
                        g.getGradeValue(),
                        g.getEnrollment().getId(),
                        g.isFinalized()
                ))
                .toList();

        if (grades.isEmpty()) {
            return new mGeneric.mAPIResponse<>(204, "No grades found", grades);
        }

        return new mGeneric.mAPIResponse<>(200, "Grades fetched", grades);
    }
    public mGeneric.mAPIResponse<GradeDTO> updateGrade(GradeDTO dto) {
        mGrades grade = repo.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        mEnrollments enrollment = grade.getEnrollment();
        if (!enrollment.isActive() || enrollment.isCompleted() || grade.isFinalized()) {
            return new mGeneric.mAPIResponse<>(400, "Cannot update grade: enrollment inactive, completed, or grade finalized");
        }

        // Validate grade
        if (!List.of("A","B","C","D","F").contains(dto.getGradeValue().toUpperCase())) {
            return new mGeneric.mAPIResponse<>(400, "Invalid grade value. Must be A, B, C, D, or F");
        }

        grade.setGradeType(dto.getGradeType());
        grade.setGradeValue(dto.getGradeValue().toUpperCase());
        grade.setFinalized(dto.isFinalized());

        mGrades saved = repo.save(grade);

        dto.setId(saved.getId());
        return new mGeneric.mAPIResponse<>(200, "Grade updated", dto);
    }

}