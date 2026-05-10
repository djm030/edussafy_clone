package com.edussafy.clone.domain.course.dto.response;

import com.edussafy.clone.domain.course.domain.enums.CourseStatus;
import java.time.LocalDate;

public record CourseResponse(Long id, String title, String description, Integer generation, String region,
                             Integer classNo, String instructorName, CourseStatus status,
                             LocalDate startDate, LocalDate endDate) {
}
