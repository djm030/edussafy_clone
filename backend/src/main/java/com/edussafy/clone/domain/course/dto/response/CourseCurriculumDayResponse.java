package com.edussafy.clone.domain.course.dto.response;

import java.time.LocalDate;
import java.util.List;

public record CourseCurriculumDayResponse(
        LocalDate date,
        String timeRange,
        String message,
        List<CourseSessionResponse> sessions
) { }
