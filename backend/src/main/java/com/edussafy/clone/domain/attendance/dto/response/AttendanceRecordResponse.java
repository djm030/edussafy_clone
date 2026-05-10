package com.edussafy.clone.domain.attendance.dto.response;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceReasonStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceRecordResponse(Long id, Long courseId, Long calendarDayId, LocalDate attendanceDate,
                                       LocalDateTime checkInAt, LocalDateTime checkOutAt, AttendanceStatus status,
                                       String issueTypes, AttendanceReasonStatus reasonStatus, String reasonText,
                                       String checkInType, String checkOutType, String note) { }
