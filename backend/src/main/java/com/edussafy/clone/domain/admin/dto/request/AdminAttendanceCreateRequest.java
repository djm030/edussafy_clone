package com.edussafy.clone.domain.admin.dto.request;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceReasonStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminAttendanceCreateRequest(@NotNull Long userId, Long courseId, Long calendarDayId, @NotNull LocalDate attendanceDate,
                                           LocalDateTime checkInAt, LocalDateTime checkOutAt, AttendanceStatus status, String issueTypes,
                                           AttendanceReasonStatus reasonStatus, String reasonText, String checkInType, String checkOutType, String note) {
}
