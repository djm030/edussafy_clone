package com.edussafy.clone.domain.user.api;

import com.edussafy.clone.domain.user.application.UserCommandService;
import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.request.UserProfileUpdateRequest;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
}
