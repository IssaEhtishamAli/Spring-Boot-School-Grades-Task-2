package com.school.management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseProgressDTO {
    private Long courseId;
    private String courseName;
    private List<GradeDTO> grades;
}
