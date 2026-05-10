package com.edussafy.clone.global.file;

public record FileUploadResponse(
        Long id,
        String originalName,
        String fileUrl,
        String fileType,
        Long fileSize,
        FileTargetType targetType,
        Long targetId,
        FileRole fileRole
) {
}
