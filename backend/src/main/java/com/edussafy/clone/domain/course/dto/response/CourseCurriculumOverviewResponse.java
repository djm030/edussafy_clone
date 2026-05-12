package com.edussafy.clone.domain.course.dto.response;

import java.util.List;

public record CourseCurriculumOverviewResponse(
        CourseResponse course,
        List<CourseCurriculumPhaseResponse> phases,
        List<CourseWeekResponse> weeks,
        List<CourseCurriculumDayResponse> days
) { }
