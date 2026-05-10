package com.edussafy.clone.domain.attendance.dto.request;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceIssueType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AttendanceAppealCreateRequest(
        @NotNull Long attendanceRecordId,
        @NotNull AttendanceIssueType appealType,
        @NotBlank String reason,
        Long attachmentFileId
) { }
