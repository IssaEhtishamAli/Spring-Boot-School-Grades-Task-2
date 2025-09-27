package com.school.management.Repositriy;

import com.school.management.Models.Grades.mGrades;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<mGrades, Long> {
    List<mGrades> findByEnrollmentId(Long enrollmentId);
}

