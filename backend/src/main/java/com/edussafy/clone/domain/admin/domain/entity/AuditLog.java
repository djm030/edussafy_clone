package com.edussafy.clone.domain.admin.domain.entity;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
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
@Table(name = "audit_logs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "actor_id")
    private User actor;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AuditAction action;
    @Column(nullable = false)
    private String targetType;
    private Long targetId;
    private String message;
    private String ipAddress;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public AuditLog(Long id, User actor, AuditAction action, String targetType, Long targetId, String message, String ipAddress) {
        this.id = id; this.actor = actor; this.action = action; this.targetType = targetType;
        this.targetId = targetId; this.message = message; this.ipAddress = ipAddress; this.createdAt = LocalDateTime.now();
    }
}
