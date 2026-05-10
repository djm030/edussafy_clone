package com.edussafy.clone.domain.user.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_stats")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Integer scholarshipPoint;
    private Integer totalExp;
    private String levelName;
    private Integer levelNo;
    private Double attendanceRate;
    private Integer completedLearningCount;
    private LocalDateTime updatedAt;

    @Builder
    public UserStat(Long id, User user, Integer scholarshipPoint, Integer totalExp, String levelName,
                    Integer levelNo, Double attendanceRate, Integer completedLearningCount,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.scholarshipPoint = scholarshipPoint;
        this.totalExp = totalExp;
        this.levelName = levelName;
        this.levelNo = levelNo;
        this.attendanceRate = attendanceRate;
        this.completedLearningCount = completedLearningCount;
        this.updatedAt = updatedAt;
    }
}
