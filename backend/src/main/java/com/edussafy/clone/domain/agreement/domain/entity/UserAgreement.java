package com.edussafy.clone.domain.agreement.domain.entity;

import com.edussafy.clone.domain.user.domain.entity.User;
import jakarta.persistence.Column;
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
@Table(name = "user_agreements")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAgreement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "agreement_id", nullable = false)
    private Agreement agreement;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String agreedVersion;
    @Column(nullable = false)
    private LocalDateTime agreedAt;
    private String ipAddress;
    private String userAgent;
    @Builder
    public UserAgreement(Long id, Agreement agreement, User user, String agreedVersion, LocalDateTime agreedAt, String ipAddress, String userAgent) {
        this.id=id; this.agreement=agreement; this.user=user; this.agreedVersion=agreedVersion; this.agreedAt=agreedAt == null ? LocalDateTime.now() : agreedAt; this.ipAddress=ipAddress; this.userAgent=userAgent;
    }
}
