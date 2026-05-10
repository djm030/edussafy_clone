package com.edussafy.clone.domain.auth.api;

import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Long TEMP_USER_ID = 1L;

    private final UserQueryService userQueryService;

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> me() {
        return ApiResponse.ok(userQueryService.getMe(TEMP_USER_ID));
    }
}
