package com.edussafy.clone.domain.dashboard.api;

import com.edussafy.clone.domain.dashboard.application.DashboardService;
import com.edussafy.clone.domain.dashboard.dto.response.DashboardResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/my")
    public ApiResponse<DashboardResponse> my(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(dashboardService.getMyDashboard(currentUserId));
    }
}
