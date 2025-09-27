package com.school.management.Services;

import com.school.management.Dto.*;
import com.school.management.Models.Enrollments.mEnrollments;
import com.school.management.Models.Generic.mGeneric;
import com.school.management.Models.Grades.mGrades;
import com.school.management.Models.Student.mStudent;
import com.school.management.Repositriy.EnrollmentRepository;
import com.school.management.Repositriy.StudentRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class StudentService {

    private final StudentRepository _studentRepo;
    private final EnrollmentRepository _enrollmentRepo;


    public StudentService(StudentRepository repo, EnrollmentRepository enrollmentRepo) {
        this._studentRepo = repo;
        this._enrollmentRepo =enrollmentRepo;
    }

    public mGeneric.mAPIResponse<StudentDTO> addStudent(StudentDTO dto) {
        if (_studentRepo.findByEmail(dto.getEmail()).isPresent()) {
            return new mGeneric.mAPIResponse<>(400, "Email already exists");
        }
        mStudent s = new mStudent();
        s.setFirstName(dto.getFirstName());
        s.setLastName(dto.getLastName());
        s.setEmail(dto.getEmail());
        s.setDateOfBirth(dto.getDateOfBirth());
        s.setPhoneNumber(dto.getPhoneNumber());

        mStudent saved = _studentRepo.save(s);
        dto.setId(saved.getId());

        return new mGeneric.mAPIResponse<>(201, "Student created", dto);
    }

    public mGeneric.mAPIResponse<List<StudentDTO>> getAllStudents() {
        List<StudentDTO> students = _studentRepo.findAll().stream()
                .map(s -> new StudentDTO(
                        s.getId(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getEmail(),
                        s.getDateOfBirth(),
                        s.getPhoneNumber()
                ))
                .toList();

        if (students.isEmpty()) {
            return new mGeneric.mAPIResponse<>(404, "No students found");
        }

        return new mGeneric.mAPIResponse<>(200, "Students fetched", students);
    }
    public mGeneric.mAPIResponse<StudentDTO> updateStudent(Long id, StudentDTO dto) {
        mStudent student = _studentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // if email is changing and already exists
        if (!student.getEmail().equals(dto.getEmail()) && _studentRepo.findByEmail(dto.getEmail()).isPresent()) {
            return new mGeneric.mAPIResponse<>(400, "Email already exists");
        }

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setPhoneNumber(dto.getPhoneNumber());

        mStudent updated = _studentRepo.save(student);
        dto.setId(updated.getId());

        return new mGeneric.mAPIResponse<>(200, "Student updated", dto);
    }
    public mGeneric.mAPIResponse<StudentProgressDTO> getStudentProgress(Long studentId) {
        mStudent student = _studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<mEnrollments> enrollments = _enrollmentRepo.findByStudentId(studentId);

        List<CourseProgressDTO> courseProgress = enrollments.stream().map(enrollment -> {
            List<GradeDTO> grades = enrollment.getGrades().stream()
                    .map(g -> new GradeDTO(
                            g.getId(),
                            g.getGradeType(),
                            g.getGradeValue(),
                            enrollment.getId(),
                            g.isFinalized()
                    ))
                    .toList();

            return new CourseProgressDTO(
                    enrollment.getCourse().getId(),
                    enrollment.getCourse().getCourseName(),
                    grades
            );
        }).toList();

        double gpa = gpaCalCulation(enrollments);

        StudentProgressDTO dto = new StudentProgressDTO(
                student.getId(),
                student.getFirstName() + " " + student.getLastName(),
                courseProgress,
                gpa
        );

        return new mGeneric.mAPIResponse<>(200, "Academic progress fetched", dto);
    }
    public mGeneric.mAPIResponse<PaginatedResponse<StudentDTO>> getStudentsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<mStudent> studentPage = _studentRepo.findAll(pageable);

        List<StudentDTO> students = studentPage.getContent().stream()
                .map(s -> new StudentDTO(
                        s.getId(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getEmail(),
                        s.getDateOfBirth(),
                        s.getPhoneNumber()
                ))
                .toList();

        PaginatedResponse<StudentDTO> response = new PaginatedResponse<>(
                students,
                studentPage.getTotalElements(),
                studentPage.getNumber(),
                studentPage.getSize()
        );

        return new mGeneric.mAPIResponse<>(200, "Students fetched", response);
    }


    private double gpaCalCulation(List<mEnrollments> enrollments) {
        List<Double> points = new ArrayList<>();

        for (mEnrollments enrollment : enrollments) {
            for (mGrades grade : enrollment.getGrades()) {
                points.add(mapingGradeToPoint(grade.getGradeValue()));
            }
        }

        if (points.isEmpty()) return 0.0;

        double total = points.stream().mapToDouble(Double::doubleValue).sum();
        return Math.round((total / points.size()) * 100.0) / 100.0; // 2 decimal
    }

    private double mapingGradeToPoint(String grade) {
        return switch (grade.toUpperCase()) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            case "F" -> 0.0;
            default -> 0.0;
        };
    }
    public mGeneric.mAPIResponse<String> deleteStudent(Long studentId) {
        mStudent student = _studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        boolean hasEnrollments = !student.getEnrollments().isEmpty();
        if (hasEnrollments) {
            return new mGeneric.mAPIResponse<>(400, "Cannot delete student with active enrollments", null);
        }

        _studentRepo.delete(student);
        return new mGeneric.mAPIResponse<>(200, "Student deleted successfully", "Deleted student ID: " + studentId);
    }

}
