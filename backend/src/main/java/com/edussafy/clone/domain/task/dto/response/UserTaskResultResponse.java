package com.edussafy.clone.domain.task.dto.response;

import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserTaskResultResponse(Long id, Long taskId, TaskResultStatus resultStatus, BigDecimal score,
                                     BigDecimal originalScore, BigDecimal retakeScore, Integer attemptCount,
                                     String answerData, LocalDateTime submittedAt, LocalDateTime completedAt,
                                     LocalDateTime updatedAt) { }
