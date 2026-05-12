package com.edussafy.clone.domain.attendance.dto.response;

import com.edussafy.clone.domain.attendance.domain.enums.AttendanceAppealStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceReasonStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.attendance.domain.enums.EducationDayType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AttendanceMonthlyDayResponse(
        LocalDate date,
        Long calendarDayId,
        Long attendanceRecordId,
        boolean educationDay,
        EducationDayType dayType,
        String title,
        AttendanceStatus status,
        AttendanceReasonStatus reasonStatus,
        LocalDateTime checkInAt,
        LocalDateTime checkOutAt,
        Long appealId,
        AttendanceAppealStatus appealStatus,
        boolean canAppeal
) { }
