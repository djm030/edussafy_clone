package com.edussafy.clone.domain.survey.domain.entity;

import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.survey.domain.enums.EventType;
import com.edussafy.clone.domain.survey.domain.enums.FormType;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.global.entity.BaseTimeEntity;
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
@Table(name = "surveys")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Survey extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "category_id")
    private SurveyCategory category;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private FormType formType;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    @Column(nullable = false)
    private Boolean isRequired;
    @Enumerated(EnumType.STRING)
    private EventType eventType;
    private String location;
    private Integer capacity;
    @Column(columnDefinition = "TEXT")
    private String selectionPolicy;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "linked_post_id")
    private BoardPost linkedPost;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "created_by_id")
    private User createdBy;
    @Builder
    public Survey(Long id, SurveyCategory category, String title, String description, FormType formType, LocalDateTime openAt, LocalDateTime closeAt, Boolean isRequired, EventType eventType, String location, Integer capacity, String selectionPolicy, BoardPost linkedPost, User createdBy) {
        this.id=id; this.category=category; this.title=title; this.description=description; this.formType=formType; this.openAt=openAt; this.closeAt=closeAt; this.isRequired=isRequired == null ? false : isRequired; this.eventType=eventType; this.location=location; this.capacity=capacity; this.selectionPolicy=selectionPolicy; this.linkedPost=linkedPost; this.createdBy=createdBy;
    }
}
