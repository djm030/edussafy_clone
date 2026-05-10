package com.edussafy.clone.domain.course.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "course_weeks")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseWeek {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;
    @Column(nullable = false)
    private Integer weekNo;
    @Column(nullable = false)
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    @Column(nullable = false)
    private Integer sortOrder;

    @Builder
    public CourseWeek(Long id, Course course, Integer weekNo, String title, LocalDate startDate, LocalDate endDate, Integer sortOrder) {
        this.id = id;
        this.course = course;
        this.weekNo = weekNo;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortOrder = sortOrder == null ? 0 : sortOrder;
    }

    public void update(Integer weekNo, String title, LocalDate startDate, LocalDate endDate, Integer sortOrder) {
        this.weekNo = weekNo;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortOrder = sortOrder == null ? this.sortOrder : sortOrder;
    }
}
