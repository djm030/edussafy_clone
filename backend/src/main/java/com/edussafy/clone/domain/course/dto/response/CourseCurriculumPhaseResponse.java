package com.edussafy.clone.domain.course.dto.response;

public record CourseCurriculumPhaseResponse(
        String label,
        String status,
        boolean active
) { }
