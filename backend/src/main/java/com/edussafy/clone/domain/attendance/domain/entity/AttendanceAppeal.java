package com.edussafy.clone.domain.attendance.domain.entity;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceAppealStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceIssueType;
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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "attendance_appeals")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AttendanceAppeal extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "attendance_record_id", nullable = false)
    private AttendanceRecord attendanceRecord;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AttendanceIssueType appealType;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "attachment_file_id")
    private FileResource attachmentFile;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private AttendanceAppealStatus appealStatus;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "reviewed_by_id")
    private User reviewedBy;
    private LocalDateTime reviewedAt;
    @Column(columnDefinition = "TEXT")
    private String reviewComment;

    @Builder
    public AttendanceAppeal(Long id, AttendanceRecord attendanceRecord, User user, AttendanceIssueType appealType,
                            String reason, FileResource attachmentFile, AttendanceAppealStatus appealStatus) {
        this.id = id; this.attendanceRecord = attendanceRecord; this.user = user; this.appealType = appealType;
        this.reason = reason; this.attachmentFile = attachmentFile;
        this.appealStatus = appealStatus == null ? AttendanceAppealStatus.SUBMITTED : appealStatus;
    }
}
