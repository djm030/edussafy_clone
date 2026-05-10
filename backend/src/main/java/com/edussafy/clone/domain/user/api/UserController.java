package com.edussafy.clone.domain.user.api;

import com.edussafy.clone.domain.user.application.UserCommandService;
import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.request.PasswordVerifyRequest;
import com.edussafy.clone.domain.user.dto.request.UserProfileUpdateRequest;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.dto.response.PasswordVerifyResponse;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> getMe(@CurrentUser Long userId) {
        return ApiResponse.ok(userQueryService.getMe(userId));
    }

    @PatchMapping("/me")
    public ApiResponse<Void> updateMe(@CurrentUser Long userId, @Valid @RequestBody UserProfileUpdateRequest request) {
        userCommandService.updateProfile(userId, request.toCommand());
        return ApiResponse.ok();
    }

    @PostMapping("/me/password/verify")
    public ApiResponse<PasswordVerifyResponse> verifyPassword(
            @CurrentUser Long userId,
            @Valid @RequestBody PasswordVerifyRequest request
    ) {
        boolean verified = userCommandService.verifyPassword(userId, request.toCommand());
        return ApiResponse.ok(new PasswordVerifyResponse(verified));
    }

    @GetMapping("/me/campus-summary")
    public ApiResponse<CampusSummaryResponse> getCampusSummary(@CurrentUser Long userId) {
        return ApiResponse.ok(userQueryService.getCampusSummary(userId));
    }
}
