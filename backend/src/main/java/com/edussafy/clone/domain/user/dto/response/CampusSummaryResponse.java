package com.edussafy.clone.domain.user.dto.response;

public record CampusSummaryResponse(
        UserMeResponse user,
        Integer scholarshipPoint,
        Integer totalExp,
        String levelName,
        Integer levelNo,
        Double attendanceRate,
        Integer completedLearningCount,
        long unreadNotificationCount
) {
}
