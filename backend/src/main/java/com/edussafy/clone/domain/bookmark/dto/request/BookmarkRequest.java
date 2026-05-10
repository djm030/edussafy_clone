package com.edussafy.clone.domain.bookmark.dto.request;

import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import jakarta.validation.constraints.NotNull;

public record BookmarkRequest(@NotNull BookmarkTargetType targetType, @NotNull Long targetId) { }
