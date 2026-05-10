package com.edussafy.clone.domain.task.domain.entity;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseSession;
import com.edussafy.clone.domain.survey.domain.entity.Survey;
import com.edussafy.clone.domain.task.domain.enums.CourseTaskType;
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
@Table(name = "course_tasks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseTask extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "session_id")
    private CourseSession session;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "survey_id")
    private Survey survey;
    @Column(nullable = false)
    private String title;
    private Integer roundNo;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private CourseTaskType taskType;
    @Column(columnDefinition = "TEXT")
    private String description;
    private LocalDateTime openAt;
    private LocalDateTime closeAt;
    private Integer totalScore;
    @Column(nullable = false)
    private Boolean isRequired;
    @Column(nullable = false)
    private Integer sortOrder;
    @Builder
    public CourseTask(Long id, Course course, CourseSession session, Survey survey, String title, Integer roundNo, CourseTaskType taskType, String description, LocalDateTime openAt, LocalDateTime closeAt, Integer totalScore, Boolean isRequired, Integer sortOrder) {
        this.id=id; this.course=course; this.session=session; this.survey=survey; this.title=title; this.roundNo=roundNo; this.taskType=taskType; this.description=description; this.openAt=openAt; this.closeAt=closeAt; this.totalScore=totalScore; this.isRequired=isRequired == null ? false : isRequired; this.sortOrder=sortOrder == null ? 0 : sortOrder;
    }
}
