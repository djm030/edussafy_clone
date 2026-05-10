package com.edussafy.clone.domain.task.domain.entity;

import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_task_results")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTaskResult {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "task_id", nullable = false)
    private CourseTask task;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private TaskResultStatus resultStatus;
    private BigDecimal score;
    private BigDecimal originalScore;
    private BigDecimal retakeScore;
    @Column(nullable = false)
    private Integer attemptCount;
    @Column(columnDefinition = "json")
    private String answerData;
    private LocalDateTime submittedAt;
    private LocalDateTime completedAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Builder
    public UserTaskResult(Long id, CourseTask task, User user, TaskResultStatus resultStatus, BigDecimal score, BigDecimal originalScore, BigDecimal retakeScore, Integer attemptCount, String answerData) {
        this.id=id; this.task=task; this.user=user; this.resultStatus=resultStatus == null ? TaskResultStatus.SCHEDULED : resultStatus; this.score=score; this.originalScore=originalScore; this.retakeScore=retakeScore; this.attemptCount=attemptCount == null ? 0 : attemptCount; this.answerData=answerData; this.updatedAt=LocalDateTime.now();
    }
    public void submit(String answerData) { this.answerData=answerData; this.resultStatus=TaskResultStatus.SUBMITTED; this.attemptCount=this.attemptCount+1; this.submittedAt=LocalDateTime.now(); this.updatedAt=LocalDateTime.now(); }

    public void updateResult(TaskResultStatus resultStatus, BigDecimal score, BigDecimal originalScore, BigDecimal retakeScore, String answerData) {
        this.resultStatus = resultStatus;
        this.score = score;
        this.originalScore = originalScore;
        this.retakeScore = retakeScore;
        this.answerData = answerData;
        this.completedAt = (resultStatus == TaskResultStatus.COMPLETED || resultStatus == TaskResultStatus.PASSED || resultStatus == TaskResultStatus.FAILED) ? LocalDateTime.now() : this.completedAt;
        this.updatedAt = LocalDateTime.now();
    }
}
