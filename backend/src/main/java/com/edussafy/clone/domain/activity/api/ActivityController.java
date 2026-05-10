package com.edussafy.clone.domain.activity.api;

import com.edussafy.clone.domain.activity.application.ActivityService;
import com.edussafy.clone.domain.activity.domain.enums.ActivityType;
import com.edussafy.clone.domain.activity.dto.response.UserActivityRecordResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activities")
public class ActivityController {
    private final ActivityService activityService;

    @GetMapping("/my")
    public ApiResponse<PageResponse<UserActivityRecordResponse>> getMyActivities(@CurrentUser Long currentUserId,
            @RequestParam(required = false) ActivityType activityType, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(activityService.getMyActivities(currentUserId, activityType, page, size));
    }
}
