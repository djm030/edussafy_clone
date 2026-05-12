package com.edussafy.clone.domain.attendance.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record AttendanceTodayResponse(
        AttendanceRecordResponse record,
        LocalDate currentDate,
        LocalTime currentTime,
        boolean canCheckIn,
        boolean canCheckOut,
        String nextAction,
        String message
) { }
