package com.school.management.Services;

import com.school.management.Dto.CourseDTO;
import com.school.management.Dto.PaginatedResponse;
import com.school.management.Models.Courses.mCourses;
import com.school.management.Models.Generic.mGeneric;
import com.school.management.Repositriy.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    private final CourseRepository repo;

    public CourseService(CourseRepository repo) {
        this.repo = repo;
    }

    public mGeneric.mAPIResponse<CourseDTO> addCourse(CourseDTO dto) {
        if (repo.findByCourseCode(dto.getCourseCode()).isPresent()) {
            return new mGeneric.mAPIResponse<>(400, "Course code already exists");
        }

        mCourses c = new mCourses();
        c.setCourseName(dto.getCourseName());
        c.setCourseCode(dto.getCourseCode());
        c.setCapacity(dto.getCapacity());

        // Pre-requisite mapping
        if (dto.getPreRequisiteCourseId() != null) {
            repo.findById(dto.getPreRequisiteCourseId()).ifPresent(c::setPreRequisiteCourse);
        }

        mCourses saved = repo.save(c);
        dto.setId(saved.getId());

        return new mGeneric.mAPIResponse<>(201, "Course created", dto);
    }

    public mGeneric.mAPIResponse<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = repo.findAll().stream()
                .map(c -> new CourseDTO(c.getId(), c.getCourseName(), c.getCourseCode(),
                        c.getCapacity(),
                        c.getPreRequisiteCourse() != null ? c.getPreRequisiteCourse().getId() : null))
                .toList();

        return new mGeneric.mAPIResponse<>(200, "Courses fetched", courses);
    }

    //courses pagination
    public mGeneric.mAPIResponse<PaginatedResponse<CourseDTO>> getCoursesPaginated(int page, int size) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<mCourses> coursePage = repo.findAll(pageable);

        List<CourseDTO> courses = coursePage.getContent().stream()
                .map(c -> new CourseDTO(
                        c.getId(),
                        c.getCourseName(),
                        c.getCourseCode(),
                        c.getCapacity(),
                        c.getPreRequisiteCourse() != null ? c.getPreRequisiteCourse().getId() : null
                ))
                .toList();

        long total = courses.isEmpty() ? 0 : coursePage.getTotalElements();

        PaginatedResponse<CourseDTO> response = new PaginatedResponse<>(
                courses,
                total,
                coursePage.getNumber(),
                coursePage.getSize()
        );

        return new mGeneric.mAPIResponse<>(200, "Courses fetched", response);
    }
    //updateCourse
    public mGeneric.mAPIResponse<CourseDTO> updateCourse(Long courseId, CourseDTO dto) {
        Optional<mCourses> optionalCourse = repo.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new mGeneric.mAPIResponse<>(404, "Course not found", null);
        }

        mCourses course = optionalCourse.get();

        // Check for unique course code if it's being updated
        if (!course.getCourseCode().equals(dto.getCourseCode())) {
            if (repo.findByCourseCode(dto.getCourseCode()).isPresent()) {
                return new mGeneric.mAPIResponse<>(400, "Course code already exists", null);
            }
        }

        course.setCourseName(dto.getCourseName());
        course.setCourseCode(dto.getCourseCode());
        course.setCapacity(dto.getCapacity());

        // Update pre-requisite
        if (dto.getPreRequisiteCourseId() != null) {
            repo.findById(dto.getPreRequisiteCourseId()).ifPresent(course::setPreRequisiteCourse);
        } else {
            course.setPreRequisiteCourse(null);
        }

        mCourses updated = repo.save(course);

        CourseDTO updatedDto = new CourseDTO(
                updated.getId(),
                updated.getCourseName(),
                updated.getCourseCode(),
                updated.getCapacity(),
                updated.getPreRequisiteCourse() != null ? updated.getPreRequisiteCourse().getId() : null
        );

        return new mGeneric.mAPIResponse<>(200, "Course updated successfully", updatedDto);
    }
    //deleteCourse
    public mGeneric.mAPIResponse<String> deleteCourse(Long courseId) {
        Optional<mCourses> optionalCourse = repo.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return new mGeneric.mAPIResponse<>(404, "Course not found", null);
        }

        mCourses course = optionalCourse.get();

        // Prevent deletion if students are enrolled
        if (!course.getEnrollments().isEmpty()) {
            return new mGeneric.mAPIResponse<>(400, "Cannot delete course with enrolled students", null);
        }

        repo.delete(course);
        return new mGeneric.mAPIResponse<>(200, "Course deleted successfully", "Deleted course ID: " + courseId);
    }

}
