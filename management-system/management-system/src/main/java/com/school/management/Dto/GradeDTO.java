package com.school.management.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// GradeDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO {
    private Long id;
    private String gradeType;
    private String gradeValue;
    private Long enrollmentId;
    private boolean finalized;
}


