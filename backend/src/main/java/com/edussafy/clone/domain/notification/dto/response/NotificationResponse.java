package com.edussafy.clone.domain.notification.dto.response;

import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import java.time.LocalDateTime;

public record NotificationResponse(Long id, String title, String content, NotificationType notificationType,
                                   String targetType, Long targetId, Boolean isImportant, String metadata,
                                   Boolean isRead, LocalDateTime readAt, LocalDateTime createdAt) { }
