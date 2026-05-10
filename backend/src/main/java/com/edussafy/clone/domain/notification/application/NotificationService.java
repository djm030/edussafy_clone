package com.edussafy.clone.domain.notification.application;

import com.edussafy.clone.domain.notification.domain.entity.Notification;
import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.notification.domain.repository.NotificationRepository;
import com.edussafy.clone.domain.notification.dto.mapper.NotificationDtoMapper;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import com.edussafy.clone.domain.notification.exception.NotificationNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;
import com.edussafy.clone.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationDtoMapper mapper;

    public PageResponse<NotificationResponse> getMyNotifications(Long userId, Boolean isRead, NotificationType notificationType, int page, int size) {
        User user = getUser(userId);
        if (isRead != null && notificationType != null) return PageResponse.from(notificationRepository.findByReceiverAndIsReadAndNotificationTypeOrderByCreatedAtDesc(user, isRead, notificationType, PageRequest.of(page, size)).map(mapper::toResponse));
        if (isRead != null) return PageResponse.from(notificationRepository.findByReceiverAndIsReadOrderByCreatedAtDesc(user, isRead, PageRequest.of(page, size)).map(mapper::toResponse));
        if (notificationType != null) return PageResponse.from(notificationRepository.findByReceiverAndNotificationTypeOrderByCreatedAtDesc(user, notificationType, PageRequest.of(page, size)).map(mapper::toResponse));
        return PageResponse.from(notificationRepository.findByReceiverOrderByCreatedAtDesc(user, PageRequest.of(page, size)).map(mapper::toResponse));
    }
    public long getUnreadCount(Long userId) { return notificationRepository.countByReceiverAndIsReadFalse(getUser(userId)); }
    @Transactional
    public NotificationResponse markRead(Long notificationId, Long userId) {
        Notification notification = getMyNotification(notificationId, userId);
        notification.markRead();
        return mapper.toResponse(notification);
    }
    @Transactional
    public void markAllRead(Long userId) { notificationRepository.findByReceiverAndIsReadFalse(getUser(userId)).forEach(Notification::markRead); }
    @Transactional
    public void delete(Long notificationId, Long userId) { notificationRepository.delete(getMyNotification(notificationId, userId)); }
    private Notification getMyNotification(Long notificationId, Long userId) {
        Notification n = notificationRepository.findById(notificationId).orElseThrow(NotificationNotFoundException::new);
        if (!n.getReceiver().getId().equals(userId)) throw new BusinessException(ErrorCode.ACCESS_DENIED);
        return n;
    }
    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
}
