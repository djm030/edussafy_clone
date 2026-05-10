package com.edussafy.clone.domain.course.domain.entity;

import com.edussafy.clone.domain.course.domain.enums.CourseStatus;
import com.edussafy.clone.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "courses")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Integer generation;
    private String region;
    private Integer classNo;
    private String instructorName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public Course(Long id, String title, String description, Integer generation, String region, Integer classNo,
                  String instructorName, CourseStatus status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.generation = generation;
        this.region = region;
        this.classNo = classNo;
        this.instructorName = instructorName;
        this.status = status == null ? CourseStatus.PLANNED : status;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
