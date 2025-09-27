package com.school.management.Models.Grades;

import com.school.management.Models.Base.mBase;
import com.school.management.Models.Enrollments.mEnrollments;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity
@Table(name = "grades")
public class mGrades extends mBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private mEnrollments enrollment;

    @NotBlank(message = "Grade type is required")
    @Column(nullable = false)
    private String gradeType; // e.g. Midterm, Final, Assignment

    @Pattern(regexp = "A|B|C|D|F", message = "Grade must be A, B, C, D, or F")
    @Column(nullable = false)
    private String gradeValue;

    @Column(nullable = false)
    private boolean finalized = false; // Once true, no more updates allowed
}
