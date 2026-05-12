package com.edussafy.clone.domain.dashboard.dto.response;

import com.edussafy.clone.domain.attendance.dto.response.AttendanceTodayResponse;
import com.edussafy.clone.domain.board.dto.response.BoardPostListResponse;
import com.edussafy.clone.domain.course.dto.response.CourseSessionResponse;
import com.edussafy.clone.domain.learning.dto.response.LearningContentResponse;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.task.dto.response.CourseTaskResponse;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import java.util.List;

public record DashboardResponse(
        UserMeResponse user,
        CampusSummaryResponse campusSummary,
        PointSummaryResponse pointSummary,
        AttendanceTodayResponse todayAttendance,
        List<NotificationResponse> notifications,
        List<CourseSessionResponse> curriculumPreview,
        List<CourseTaskResponse> questPreview,
        List<LearningContentResponse> learningPreview,
        List<BoardPostListResponse> freeBoardPosts,
        List<BoardPostListResponse> notices
) {
}
