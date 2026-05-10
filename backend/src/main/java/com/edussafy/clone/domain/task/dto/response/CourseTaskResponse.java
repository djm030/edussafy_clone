package com.edussafy.clone.domain.task.dto.response;

import com.edussafy.clone.domain.task.domain.enums.CourseTaskType;
import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import java.time.LocalDateTime;

public record CourseTaskResponse(Long id, Long courseId, Long sessionId, Long surveyId, String title, Integer roundNo,
                                 CourseTaskType taskType, String description, LocalDateTime openAt, LocalDateTime closeAt,
                                 Integer totalScore, Boolean isRequired, Integer sortOrder, TaskResultStatus myResultStatus) { }
