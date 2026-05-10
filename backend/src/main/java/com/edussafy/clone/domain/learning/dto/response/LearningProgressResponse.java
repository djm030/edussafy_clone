package com.edussafy.clone.domain.learning.dto.response;

import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
import java.time.LocalDateTime;

public record LearningProgressResponse(Long id, Long contentId, String contentTitle, LearningProgressStatus progressStatus,
                                       Double progressRate, Integer lastPositionSeconds, LocalDateTime startedAt,
                                       LocalDateTime completedAt, LocalDateTime lastAccessedAt) {
}
