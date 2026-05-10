package com.edussafy.clone.domain.user.domain.entity;

import jakarta.persistence.Entity;
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
@Table(name = "password_change_histories")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordChangeHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDateTime changedAt;
    private String ipAddress;
    private String userAgent;

    @Builder
    public PasswordChangeHistory(Long id, User user, LocalDateTime changedAt, String ipAddress, String userAgent) {
        this.id = id;
        this.user = user;
        this.changedAt = changedAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }
}
