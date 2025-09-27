package com.school.management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentProgressDTO {
    private Long studentId;
    private String studentName;
    private List<CourseProgressDTO> courses;
    private double gpa;
}