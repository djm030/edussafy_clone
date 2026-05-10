package com.edussafy.clone.domain.board.dto.response;

import java.time.LocalDateTime;

public record BoardPostListResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        String displayName,
        boolean isMine,
        Integer viewCount,
        Integer likeCount,
        Integer commentCount,
        Boolean hasAttachment,
        Boolean isNotice,
        LocalDateTime createdAt
) {
}
