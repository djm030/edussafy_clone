package com.edussafy.clone.domain.bookmark.dto.response;

import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import java.time.LocalDateTime;

public record BookmarkResponse(Long id, BookmarkTargetType targetType, Long targetId, LocalDateTime createdAt) { }
