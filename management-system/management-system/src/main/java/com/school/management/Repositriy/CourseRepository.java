package com.school.management.Repositriy;

import com.school.management.Models.Courses.mCourses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<mCourses, Long> {
    Optional<mCourses> findByCourseCode(String courseCode);
}


