package com.edussafy.clone.domain.course.domain.entity;

import com.edussafy.clone.domain.board.domain.entity.BoardPost;
import com.edussafy.clone.domain.course.domain.enums.CourseSessionType;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "course_sessions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSession extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "week_id")
    private CourseWeek week;
    @Column(nullable = false)
    private String title;
    private String subtitle;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private CourseSessionType sessionType;
    private LocalDate sessionDate;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private String instructorName;
    private String location;
    private String liveUrl;
    private String replayUrl;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "material_post_id")
    private BoardPost materialPost;
    @Column(nullable = false)
    private Boolean isRequired;
    @Column(nullable = false)
    private Integer sortOrder;

    @Builder
    public CourseSession(Long id, Course course, CourseWeek week, String title, String subtitle,
                         CourseSessionType sessionType, LocalDate sessionDate, LocalDateTime startAt,
                         LocalDateTime endAt, String instructorName, String location, String liveUrl,
                         String replayUrl, BoardPost materialPost, Boolean isRequired, Integer sortOrder) {
        this.id = id;
        this.course = course;
        this.week = week;
        this.title = title;
        this.subtitle = subtitle;
        this.sessionType = sessionType;
        this.sessionDate = sessionDate;
        this.startAt = startAt;
        this.endAt = endAt;
        this.instructorName = instructorName;
        this.location = location;
        this.liveUrl = liveUrl;
        this.replayUrl = replayUrl;
        this.materialPost = materialPost;
        this.isRequired = isRequired != null && isRequired;
        this.sortOrder = sortOrder == null ? 0 : sortOrder;
    }
}
