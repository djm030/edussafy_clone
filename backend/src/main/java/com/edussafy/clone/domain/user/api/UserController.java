package com.edussafy.clone.domain.user.api;

import com.edussafy.clone.domain.user.application.UserCommandService;
import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.request.UserProfileUpdateRequest;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.response.ApiResponse;
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

    private static final Long TEMP_USER_ID = 1L;

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> getMe() {
        return ApiResponse.ok(userQueryService.getMe(TEMP_USER_ID));
    }

    @PatchMapping("/me")
    public ApiResponse<Void> updateMe(@Valid @RequestBody UserProfileUpdateRequest request) {
        userCommandService.updateProfile(TEMP_USER_ID, request.toCommand());
        return ApiResponse.ok();
    }
}
