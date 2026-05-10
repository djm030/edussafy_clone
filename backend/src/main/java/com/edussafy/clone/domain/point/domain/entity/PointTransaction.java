package com.edussafy.clone.domain.point.domain.entity;

import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
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
@Table(name = "point_transactions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private PointTransactionType transactionType;
    @Column(nullable = false)
    private Integer pointAmount;
    @Column(nullable = false)
    private Integer expAmount;
    private String reason;
    private String targetType;
    private Long targetId;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by_id")
    private User createdBy;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public PointTransaction(Long id, User user, PointTransactionType transactionType, Integer pointAmount, Integer expAmount,
                            String reason, String targetType, Long targetId, User createdBy) {
        this.id = id; this.user = user; this.transactionType = transactionType; this.pointAmount = pointAmount == null ? 0 : pointAmount;
        this.expAmount = expAmount == null ? 0 : expAmount; this.reason = reason; this.targetType = targetType;
        this.targetId = targetId; this.createdBy = createdBy; this.createdAt = LocalDateTime.now();
    }
}
