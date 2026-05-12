package com.edussafy.clone.domain.dashboard.application;

import com.edussafy.clone.domain.attendance.application.AttendanceService;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceTodayResponse;
import com.edussafy.clone.domain.board.application.BoardQueryService;
import com.edussafy.clone.domain.board.dto.response.BoardPostListResponse;
import com.edussafy.clone.domain.course.application.CourseQueryService;
import com.edussafy.clone.domain.course.dto.response.CourseResponse;
import com.edussafy.clone.domain.course.dto.response.CourseSessionResponse;
import com.edussafy.clone.domain.dashboard.dto.response.DashboardResponse;
import com.edussafy.clone.domain.learning.application.LearningQueryService;
import com.edussafy.clone.domain.learning.dto.response.LearningContentResponse;
import com.edussafy.clone.domain.notification.application.NotificationService;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import com.edussafy.clone.domain.point.application.PointService;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.task.application.TaskService;
import com.edussafy.clone.domain.task.dto.response.CourseTaskResponse;
import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final UserQueryService userQueryService;
    private final PointService pointService;
    private final AttendanceService attendanceService;
    private final NotificationService notificationService;
    private final CourseQueryService courseQueryService;
    private final TaskService taskService;
    private final LearningQueryService learningQueryService;
    private final BoardQueryService boardQueryService;

    public DashboardResponse getMyDashboard(Long userId) {
        UserMeResponse user = userQueryService.getMe(userId);
        CampusSummaryResponse campusSummary = userQueryService.getCampusSummary(userId);
        PointSummaryResponse pointSummary = pointService.getMySummary(userId);
        AttendanceTodayResponse todayAttendance = attendanceService.getToday(userId);
        List<NotificationResponse> notifications = notificationService.getMyNotifications(userId, null, null, 0, 3).content();

        Long courseId = courseQueryService.getMyCourses(userId).stream()
                .map(CourseResponse::id)
                .findFirst()
                .orElse(null);
        List<CourseSessionResponse> curriculumPreview = courseId == null
                ? List.of()
                : courseQueryService.getCourseSessionsInRange(courseId, LocalDate.now().minusDays(7), LocalDate.now().plusDays(7), 0, 5).content();
        List<CourseTaskResponse> questPreview = taskService.getMyTasks(userId, courseId, null, null, 0, 5).content();
        List<LearningContentResponse> learningPreview = learningQueryService.getContents(null, courseId, null, null, false, 0, 5).content();
        List<BoardPostListResponse> freeBoardPosts = boardQueryService.getPosts("free", null, null, 0, 5, userId).content();
        List<BoardPostListResponse> notices = boardQueryService.getPosts("notice", null, null, 0, 5, userId).content();

        return new DashboardResponse(
                user,
                campusSummary,
                pointSummary,
                todayAttendance,
                notifications,
                curriculumPreview,
                questPreview,
                learningPreview,
                freeBoardPosts,
                notices
        );
    }
}
