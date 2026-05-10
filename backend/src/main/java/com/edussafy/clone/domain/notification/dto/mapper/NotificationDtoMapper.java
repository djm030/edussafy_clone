package com.edussafy.clone.domain.notification.dto.mapper;

import com.edussafy.clone.domain.notification.domain.entity.Notification;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationDtoMapper {
    public NotificationResponse toResponse(Notification n) {
        return new NotificationResponse(n.getId(), n.getTitle(), n.getContent(), n.getNotificationType(), n.getTargetType(),
                n.getTargetId(), n.getIsImportant(), n.getMetadata(), n.getIsRead(), n.getReadAt(), n.getCreatedAt());
    }
}
