package com.edussafy.clone.domain.task.dto.mapper;

import com.edussafy.clone.domain.task.domain.entity.CourseTask;
import com.edussafy.clone.domain.task.domain.entity.UserTaskResult;
import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import com.edussafy.clone.domain.task.dto.response.CourseTaskResponse;
import com.edussafy.clone.domain.task.dto.response.UserTaskResultResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoMapper {
    public CourseTaskResponse toTaskResponse(CourseTask task, TaskResultStatus myStatus) {
        return new CourseTaskResponse(task.getId(), task.getCourse().getId(), task.getSession() == null ? null : task.getSession().getId(),
                task.getSurvey() == null ? null : task.getSurvey().getId(), task.getTitle(), task.getRoundNo(), task.getTaskType(),
                task.getDescription(), task.getOpenAt(), task.getCloseAt(), task.getTotalScore(), task.getIsRequired(), task.getSortOrder(), myStatus);
    }
    public UserTaskResultResponse toResultResponse(UserTaskResult result) {
        return new UserTaskResultResponse(result.getId(), result.getTask().getId(), result.getResultStatus(), result.getScore(),
                result.getOriginalScore(), result.getRetakeScore(), result.getAttemptCount(), result.getAnswerData(),
                result.getSubmittedAt(), result.getCompletedAt(), result.getUpdatedAt());
    }
}
