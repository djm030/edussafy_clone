package com.edussafy.clone.domain.learning.domain.entity;

import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
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
@Table(name = "user_learning_progresses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLearningProgress {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "content_id", nullable = false)
    private LearningContent content;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private LearningProgressStatus progressStatus;
    @Column(nullable = false)
    private Double progressRate;
    @Column(nullable = false)
    private Integer lastPositionSeconds;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime lastAccessedAt;

    @Builder
    public UserLearningProgress(Long id, User user, LearningContent content, LearningProgressStatus progressStatus,
                                Double progressRate, Integer lastPositionSeconds) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.progressStatus = progressStatus == null ? LearningProgressStatus.NOT_STARTED : progressStatus;
        this.progressRate = progressRate == null ? 0.0 : progressRate;
        this.lastPositionSeconds = lastPositionSeconds == null ? 0 : lastPositionSeconds;
    }

    public void saveProgress(Double progressRate, Integer lastPositionSeconds, LearningProgressStatus status) {
        LocalDateTime now = LocalDateTime.now();
        if (startedAt == null) startedAt = now;
        this.progressRate = progressRate == null ? this.progressRate : progressRate;
        this.lastPositionSeconds = lastPositionSeconds == null ? this.lastPositionSeconds : lastPositionSeconds;
        this.progressStatus = status == null ? this.progressStatus : status;
        if (this.progressStatus == LearningProgressStatus.COMPLETED) {
            this.progressRate = 100.0;
            this.completedAt = now;
        }
        this.lastAccessedAt = now;
    }

    public void complete() {
        saveProgress(100.0, lastPositionSeconds, LearningProgressStatus.COMPLETED);
    }
}
