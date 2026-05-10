package com.edussafy.clone.domain.notification.domain.entity;

import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "sender_id")
    private User sender;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private NotificationType notificationType;
    private String targetType;
    private Long targetId;
    @Column(nullable = false)
    private Boolean isImportant;
    @Column(columnDefinition = "json")
    private String metadata;
    @Column(nullable = false)
    private Boolean isRead;
    private LocalDateTime readAt;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Notification(Long id, User sender, User receiver, String title, String content, NotificationType notificationType, String targetType, Long targetId, Boolean isImportant, String metadata, Boolean isRead, LocalDateTime createdAt) {
        this.id=id; this.sender=sender; this.receiver=receiver; this.title=title; this.content=content; this.notificationType=notificationType; this.targetType=targetType; this.targetId=targetId; this.isImportant=isImportant == null ? false : isImportant; this.metadata=metadata; this.isRead=isRead == null ? false : isRead; this.createdAt=createdAt == null ? LocalDateTime.now() : createdAt;
    }
    public void markRead() { if (!Boolean.TRUE.equals(this.isRead)) { this.isRead = true; this.readAt = LocalDateTime.now(); } }
}
