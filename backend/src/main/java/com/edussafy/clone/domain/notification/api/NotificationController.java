package com.edussafy.clone.domain.notification.api;

import com.edussafy.clone.domain.notification.application.NotificationService;
import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/my")
    public ApiResponse<PageResponse<NotificationResponse>> getMyNotifications(@CurrentUser Long currentUserId,
            @RequestParam(required = false) Boolean isRead, @RequestParam(required = false) NotificationType notificationType,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(notificationService.getMyNotifications(currentUserId, isRead, notificationType, page, size));
    }
    @GetMapping("/my/unread-count")
    public ApiResponse<Long> getUnreadCount(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(notificationService.getUnreadCount(currentUserId));
    }
    @PatchMapping("/{notificationId}/read")
    public ApiResponse<NotificationResponse> markRead(@PathVariable Long notificationId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(notificationService.markRead(notificationId, currentUserId));
    }
    @PatchMapping("/my/read-all")
    public ApiResponse<Void> markAllRead(@CurrentUser Long currentUserId) {
        notificationService.markAllRead(currentUserId);
        return ApiResponse.ok(null);
    }
    @DeleteMapping("/{notificationId}")
    public ApiResponse<Void> delete(@PathVariable Long notificationId, @CurrentUser Long currentUserId) {
        notificationService.delete(notificationId, currentUserId);
        return ApiResponse.ok(null);
    }
}
