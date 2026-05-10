package com.edussafy.clone.domain.course.api;

import com.edussafy.clone.domain.course.application.CourseQueryService;
import com.edussafy.clone.domain.course.dto.response.CourseResponse;
import com.edussafy.clone.domain.course.dto.response.CourseSessionResponse;
import com.edussafy.clone.domain.course.dto.response.CourseWeekResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CourseController {
    private final CourseQueryService courseQueryService;

    @GetMapping("/courses/my")
    public ApiResponse<List<CourseResponse>> getMyCourses(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(courseQueryService.getMyCourses(currentUserId));
    }

    @GetMapping("/courses/{courseId}")
    public ApiResponse<CourseResponse> getCourse(@PathVariable Long courseId) {
        return ApiResponse.ok(courseQueryService.getCourse(courseId));
    }

    @GetMapping("/courses/{courseId}/weeks")
    public ApiResponse<List<CourseWeekResponse>> getWeeks(@PathVariable Long courseId) {
        return ApiResponse.ok(courseQueryService.getWeeks(courseId));
    }

    @GetMapping("/courses/{courseId}/weeks/{weekId}/sessions")
    public ApiResponse<PageResponse<CourseSessionResponse>> getWeekSessions(
            @PathVariable Long courseId,
            @PathVariable Long weekId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(courseQueryService.getWeekSessions(courseId, weekId, page, size));
    }

    @GetMapping("/course-sessions/{sessionId}")
    public ApiResponse<CourseSessionResponse> getSession(@PathVariable Long sessionId) {
        return ApiResponse.ok(courseQueryService.getSession(sessionId));
    }

    @GetMapping("/course-sessions/replays")
    public ApiResponse<PageResponse<CourseSessionResponse>> getReplays(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long weekId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ApiResponse.ok(courseQueryService.getReplays(courseId, weekId, keyword, page, size));
    }
}
