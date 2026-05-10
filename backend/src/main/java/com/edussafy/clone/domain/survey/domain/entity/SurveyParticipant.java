package com.edussafy.clone.domain.survey.domain.entity;

import com.edussafy.clone.domain.survey.domain.enums.ParticipantStatus;
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
@Table(name = "survey_participants")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SurveyParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(columnDefinition = "json")
    private String answers;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ParticipantStatus participantStatus;
    private LocalDateTime submittedAt;
    private LocalDateTime cancelledAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Builder
    public SurveyParticipant(Long id, Survey survey, User user, String answers, ParticipantStatus participantStatus) {
        this.id=id; this.survey=survey; this.user=user; this.answers=answers; this.participantStatus=participantStatus == null ? ParticipantStatus.TARGETED : participantStatus; this.createdAt=LocalDateTime.now(); this.updatedAt=LocalDateTime.now();
    }
    public void submit(String answers) { this.answers=answers; this.participantStatus=ParticipantStatus.SUBMITTED; this.submittedAt=LocalDateTime.now(); this.cancelledAt=null; this.updatedAt=LocalDateTime.now(); }
    public void cancel() { this.participantStatus=ParticipantStatus.CANCELLED; this.cancelledAt=LocalDateTime.now(); this.updatedAt=LocalDateTime.now(); }
}
