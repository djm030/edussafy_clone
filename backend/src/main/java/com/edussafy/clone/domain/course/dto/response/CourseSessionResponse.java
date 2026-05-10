package com.edussafy.clone.domain.course.dto.response;

import com.edussafy.clone.domain.course.domain.enums.CourseSessionType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CourseSessionResponse(Long id, Long courseId, Long weekId, String title, String subtitle,
                                    CourseSessionType sessionType, LocalDate sessionDate,
                                    LocalDateTime startAt, LocalDateTime endAt, String instructorName,
                                    String location, String liveUrl, String replayUrl, Long materialPostId,
                                    Boolean isRequired, Integer sortOrder) {
}
