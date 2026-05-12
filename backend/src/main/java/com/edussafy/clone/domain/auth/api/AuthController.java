package com.edussafy.clone.domain.auth.api;

import com.edussafy.clone.domain.auth.application.AuthService;
import com.edussafy.clone.domain.auth.dto.request.LoginRequest;
import com.edussafy.clone.domain.auth.dto.request.PasswordChangeRequest;
import com.edussafy.clone.domain.auth.dto.request.PasswordResetTemporaryRequest;
import com.edussafy.clone.domain.auth.dto.request.TokenRefreshRequest;
import com.edussafy.clone.domain.auth.dto.response.LoginResponse;
import com.edussafy.clone.domain.auth.dto.response.TemporaryPasswordResponse;
import com.edussafy.clone.domain.user.application.UserQueryService;
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
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final UserQueryService userQueryService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody TokenRefreshRequest request) {
        return ApiResponse.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.ok();
    }

    @GetMapping("/me")
    public ApiResponse<UserMeResponse> me(@CurrentUser Long userId) {
        return ApiResponse.ok(userQueryService.getMe(userId));
    }

    @PostMapping("/password/reset-temporary")
    public ApiResponse<TemporaryPasswordResponse> resetTemporaryPassword(
            @Valid @RequestBody PasswordResetTemporaryRequest request
    ) {
        return ApiResponse.ok(authService.resetTemporaryPassword(request));
    }

    @PatchMapping("/password")
    public ApiResponse<Void> changePassword(
            @CurrentUser Long userId,
            @Valid @RequestBody PasswordChangeRequest request
    ) {
        authService.changePassword(userId, request);
        return ApiResponse.ok();
    }
}
