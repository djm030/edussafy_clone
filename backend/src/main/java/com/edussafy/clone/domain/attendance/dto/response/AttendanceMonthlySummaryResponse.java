package com.edussafy.clone.domain.attendance.dto.response;

public record AttendanceMonthlySummaryResponse(
        int educationDayCount,
        int normalCount,
        int lateCount,
        int earlyLeaveCount,
        int outingCount,
        int absentCount,
        int excusedCount,
        int pendingCount,
        int appealSubmittedCount,
        int appealApprovedCount,
        int appealRejectedCount
) { }
