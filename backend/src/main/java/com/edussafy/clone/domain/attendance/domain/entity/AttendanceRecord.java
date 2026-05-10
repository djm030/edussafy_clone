package com.edussafy.clone.domain.attendance.domain.entity;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceReasonStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.course.domain.entity.Course;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "attendance_records")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceRecord extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "calendar_day_id")
    private EducationCalendarDay calendarDay;
    @Column(nullable = false)
    private LocalDate attendanceDate;
    private LocalDateTime checkInAt;
    private LocalDateTime checkOutAt;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AttendanceStatus status;
    @Column(columnDefinition = "json")
    private String issueTypes;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AttendanceReasonStatus reasonStatus;
    @Column(columnDefinition = "TEXT")
    private String reasonText;
    private String checkInType;
    private String checkOutType;
    @Column(columnDefinition = "TEXT")
    private String note;

    @Builder
    public AttendanceRecord(Long id, User user, Course course, EducationCalendarDay calendarDay, LocalDate attendanceDate,
                            LocalDateTime checkInAt, LocalDateTime checkOutAt, AttendanceStatus status, String issueTypes,
                            AttendanceReasonStatus reasonStatus, String reasonText, String checkInType, String checkOutType, String note) {
        this.id = id; this.user = user; this.course = course; this.calendarDay = calendarDay; this.attendanceDate = attendanceDate;
        this.checkInAt = checkInAt; this.checkOutAt = checkOutAt; this.status = status == null ? AttendanceStatus.PENDING : status;
        this.issueTypes = issueTypes; this.reasonStatus = reasonStatus == null ? AttendanceReasonStatus.NONE : reasonStatus;
        this.reasonText = reasonText; this.checkInType = checkInType; this.checkOutType = checkOutType; this.note = note;
    }

    public void updateByAdmin(LocalDateTime checkInAt, LocalDateTime checkOutAt, AttendanceStatus status,
                              AttendanceReasonStatus reasonStatus, String issueTypes, String reasonText,
                              String checkInType, String checkOutType, String note) {
        if (checkInAt != null) this.checkInAt = checkInAt;
        if (checkOutAt != null) this.checkOutAt = checkOutAt;
        if (status != null) this.status = status;
        if (reasonStatus != null) this.reasonStatus = reasonStatus;
        if (issueTypes != null) this.issueTypes = issueTypes;
        if (reasonText != null) this.reasonText = reasonText;
        if (checkInType != null) this.checkInType = checkInType;
        if (checkOutType != null) this.checkOutType = checkOutType;
        if (note != null) this.note = note;
    }
}
