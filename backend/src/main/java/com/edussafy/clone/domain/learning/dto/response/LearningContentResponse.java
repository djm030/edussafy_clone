package com.edussafy.clone.domain.learning.dto.response;

import com.edussafy.clone.domain.learning.domain.enums.LearningContentType;
import java.time.LocalDateTime;

public record LearningContentResponse(Long id, Long courseId, Long sessionId, Long categoryId, String title,
                                      String description, LearningContentType contentType, String thumbnailUrl,
                                      String contentUrl, Integer durationSeconds, Integer viewCount,
                                      Integer likeCount, Integer bookmarkCount, Integer downloadCount,
                                      Boolean isRequired, LocalDateTime openAt, LocalDateTime closeAt) {
}
