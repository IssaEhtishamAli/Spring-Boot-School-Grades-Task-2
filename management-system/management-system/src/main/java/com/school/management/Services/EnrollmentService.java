package com.school.management.Services;

import com.school.management.Dto.EnrollmentDTO;
import com.school.management.Models.Courses.mCourses;
import com.school.management.Models.Enrollments.mEnrollments;
import com.school.management.Models.Generic.mGeneric;
import com.school.management.Models.Student.mStudent;
import com.school.management.Repositriy.CourseRepository;
import com.school.management.Repositriy.EnrollmentRepository;
import com.school.management.Repositriy.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository repo;
    private final StudentRepository studentRepo;
    private final CourseRepository courseRepo;

    public EnrollmentService(EnrollmentRepository repo, StudentRepository studentRepo, CourseRepository courseRepo) {
        this.repo = repo;
        this.studentRepo = studentRepo;
        this.courseRepo = courseRepo;
    }

    public mGeneric.mAPIResponse<EnrollmentDTO> enrollStudent(EnrollmentDTO dto) {
        // check already enrolled
        if (repo.existsByStudentIdAndCourseId(dto.getStudentId(), dto.getCourseId())) {
            return new mGeneric.mAPIResponse<>(400, "Student already enrolled in course");
        }

        // check student exist
        Optional<mStudent> studentOpt = studentRepo.findById(dto.getStudentId());
        if (studentOpt.isEmpty()) {
            return new mGeneric.mAPIResponse<>(404, "Student not found");
        }
        mStudent student = studentOpt.get();

        // check course exist
        Optional<mCourses> courseOpt = courseRepo.findById(dto.getCourseId());
        if (courseOpt.isEmpty()) {
            return new mGeneric.mAPIResponse<>(404, "Course not found");
        }
        mCourses course = courseOpt.get();

        // check capacity
        if (course.getEnrollments().size() >= course.getCapacity()) {
            return new mGeneric.mAPIResponse<>(400, "Course capacity full");
        }

        // check pre-requisite
        if (course.getPreRequisiteCourse() != null) {
            boolean hasPreReq = student.getEnrollments().stream()
                    .anyMatch(e -> e.getCourse().equals(course.getPreRequisiteCourse()) && e.isActive());
            if (!hasPreReq) {
                return new mGeneric.mAPIResponse<>(400, "Student has not completed pre-requisite");
            }
        }

        mEnrollments e = new mEnrollments();
        e.setStudent(student);
        e.setCourse(course);
        e.setActive(true);

        mEnrollments saved = repo.save(e);
        dto.setId(saved.getId());

        return new mGeneric.mAPIResponse<>(201, "Student enrolled", dto);
    }
}