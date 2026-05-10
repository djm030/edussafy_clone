package com.edussafy.clone.domain.attendance.domain.entity;

import com.edussafy.clone.domain.attendance.domain.enums.EducationDayType;
import com.edussafy.clone.domain.course.domain.entity.Course;
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
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "education_calendar_days")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EducationCalendarDay extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "course_id")
    private Course course;
    @Column(nullable = false)
    private LocalDate calendarDate;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private EducationDayType dayType;
    @Column(nullable = false)
    private Boolean isEducationDay;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Builder
    public EducationCalendarDay(Long id, Course course, LocalDate calendarDate, EducationDayType dayType,
                                Boolean isEducationDay, String title, String description) {
        this.id = id; this.course = course; this.calendarDate = calendarDate; this.dayType = dayType;
        this.isEducationDay = isEducationDay == null || isEducationDay; this.title = title; this.description = description;
    }

    public void update(Course course, LocalDate calendarDate, EducationDayType dayType, Boolean isEducationDay, String title, String description) {
        this.course = course;
        if (calendarDate != null) this.calendarDate = calendarDate;
        if (dayType != null) this.dayType = dayType;
        if (isEducationDay != null) this.isEducationDay = isEducationDay;
        if (title != null) this.title = title;
        if (description != null) this.description = description;
    }
}
