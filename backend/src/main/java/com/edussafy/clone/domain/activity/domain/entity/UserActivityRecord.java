package com.edussafy.clone.domain.activity.domain.entity;

import com.edussafy.clone.domain.activity.domain.enums.ActivityType;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.global.entity.BaseTimeEntity;
import com.edussafy.clone.global.file.FileResource;
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
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "user_activity_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActivityRecord extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ActivityType activityType;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String organization;
    private LocalDate activityDate;
    private String resultText;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "evidence_file_id")
    private FileResource evidenceFile;
    @Builder
    public UserActivityRecord(Long id, User user, ActivityType activityType, String title, String description, String organization, LocalDate activityDate, String resultText, FileResource evidenceFile) {
        this.id=id; this.user=user; this.activityType=activityType; this.title=title; this.description=description; this.organization=organization; this.activityDate=activityDate; this.resultText=resultText; this.evidenceFile=evidenceFile;
    }
}
