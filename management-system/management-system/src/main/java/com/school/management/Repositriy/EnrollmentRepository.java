package com.school.management.Repositriy;

import com.school.management.Models.Enrollments.mEnrollments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<mEnrollments, Long> {
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
    List<mEnrollments> findByStudentId(Long studentId);
}

