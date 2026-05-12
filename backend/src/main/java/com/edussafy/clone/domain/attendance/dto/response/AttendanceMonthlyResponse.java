package com.edussafy.clone.domain.attendance.dto.response;

import java.util.List;

public record AttendanceMonthlyResponse(
        int year,
        int month,
        AttendanceMonthlySummaryResponse summary,
        List<AttendanceMonthlyDayResponse> days,
        AttendanceTodayResponse today
) { }
