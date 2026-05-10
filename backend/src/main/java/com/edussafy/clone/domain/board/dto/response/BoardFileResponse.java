package com.edussafy.clone.domain.board.dto.response;

public record BoardFileResponse(
        Long id,
        String originalName,
        String fileUrl,
        String fileType,
        Long fileSize,
        Integer sortOrder
) {
}
