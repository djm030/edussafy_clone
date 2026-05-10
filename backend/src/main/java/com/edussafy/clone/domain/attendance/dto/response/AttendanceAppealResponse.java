package com.edussafy.clone.domain.attendance.dto.response;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceAppealStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceIssueType;
import java.time.LocalDateTime;

public record AttendanceAppealResponse(Long id, Long attendanceRecordId, AttendanceIssueType appealType, String reason,
                                       Long attachmentFileId, AttendanceAppealStatus appealStatus, Long reviewedById,
                                       LocalDateTime reviewedAt, String reviewComment, LocalDateTime createdAt) { }
