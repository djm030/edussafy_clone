package com.edussafy.clone.domain.course.dto.response;

import java.time.LocalDate;

public record CourseWeekResponse(Long id, Integer weekNo, String title, LocalDate startDate, LocalDate endDate,
                                 Integer sortOrder) {
}
