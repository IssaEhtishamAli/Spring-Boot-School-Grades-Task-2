package com.school.management.Models.Courses;

import com.school.management.Models.Base.mBase;
import com.school.management.Models.Enrollments.mEnrollments;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "courses")
public class mCourses extends mBase {

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Course code is required")
    @Column(unique = true, nullable = false)
    private String courseCode;   // 👈 ye field missing thi

    @Min(value = 0, message = "Course capacity must be zero or positive")
    private int capacity;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<mEnrollments> enrollments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pre_requisite_id")
    private mCourses preRequisiteCourse;
}
