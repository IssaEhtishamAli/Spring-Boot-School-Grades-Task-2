package com.school.management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// CourseDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private Long id;
    private String courseName;
    private String courseCode;
    private int capacity;
    private Long preRequisiteCourseId;
}



