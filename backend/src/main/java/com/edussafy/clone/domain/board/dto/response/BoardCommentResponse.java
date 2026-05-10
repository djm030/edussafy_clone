package com.edussafy.clone.domain.board.dto.response;

import java.time.LocalDateTime;

public record BoardCommentResponse(
        Long id,
        Long parentId,
        String content,
        String displayName,
        boolean isMine,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
