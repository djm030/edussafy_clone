package com.edussafy.clone.domain.notification.domain.repository;

import com.edussafy.clone.domain.notification.domain.entity.Notification;
import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    Page<Notification> findByReceiverOrderByCreatedAtDesc(User receiver, Pageable pageable);
    Page<Notification> findByReceiverAndIsReadOrderByCreatedAtDesc(User receiver, Boolean isRead, Pageable pageable);
    Page<Notification> findByReceiverAndNotificationTypeOrderByCreatedAtDesc(User receiver, NotificationType notificationType, Pageable pageable);
    Page<Notification> findByReceiverAndIsReadAndNotificationTypeOrderByCreatedAtDesc(User receiver, Boolean isRead, NotificationType notificationType, Pageable pageable);
    long countByReceiverAndIsReadFalse(User receiver);
    List<Notification> findByReceiverAndIsReadFalse(User receiver);
}
