package com.edussafy.clone.domain.board.dto.response;

import com.edussafy.clone.domain.board.domain.enums.ContentType;
import java.time.LocalDateTime;
import java.util.List;

public record BoardPostDetailResponse(
        Long id,
        Long categoryId,
        String categoryName,
        String title,
        ContentType contentType,
        String contentText,
        String contentHtml,
        String contentJson,
        String displayName,
        boolean isMine,
        Integer viewCount,
        Integer likeCount,
        Integer commentCount,
        Integer scrapCount,
        Boolean hasAttachment,
        Boolean isNotice,
        List<BoardFileResponse> files,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
