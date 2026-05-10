package com.edussafy.clone.domain.admin.dto.request;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceReasonStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import java.time.LocalDateTime;

public record AdminAttendanceUpdateRequest(LocalDateTime checkInAt, LocalDateTime checkOutAt, AttendanceStatus status, AttendanceReasonStatus reasonStatus,
                                           String issueTypes, String reasonText, String checkInType, String checkOutType, String note) {
}
