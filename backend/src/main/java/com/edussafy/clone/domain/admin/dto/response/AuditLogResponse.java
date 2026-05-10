package com.edussafy.clone.domain.admin.dto.response;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import java.time.LocalDateTime;

public record AuditLogResponse(Long id, Long actorId, String actorName, AuditAction action, String targetType, Long targetId, String message, String ipAddress, LocalDateTime createdAt) {
}
