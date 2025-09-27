package com.school.management.Models.Enrollments;

import com.school.management.Models.Base.mBase;
import com.school.management.Models.Courses.mCourses;
import com.school.management.Models.Grades.mGrades;
import com.school.management.Models.Student.mStudent;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "enrollments")
public class mEnrollments extends mBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private mStudent student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private mCourses course;

    @Column(nullable = false)
    private boolean active = true;  // enrollment status

    private boolean completed = false; // helps with grade rule

    // One enrollment can have many grades
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<mGrades> grades = new ArrayList<>();
}
