package com.edussafy.clone.domain.user.api;

import com.edussafy.clone.domain.user.application.UserCommandService;
import com.edussafy.clone.domain.user.application.UserQueryService;
import com.edussafy.clone.domain.user.dto.request.PasswordVerifyRequest;
import com.edussafy.clone.domain.user.dto.request.UserProfileUpdateRequest;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.dto.response.PasswordVerifyResponse;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PatchMapping("/me/profile-image")
    public ApiResponse<Void> updateProfileImage(@CurrentUser Long userId, @RequestBody Map<String, Object> request) {
        Object fileId = request.get("fileId");
        userCommandService.updateProfileImage(userId, fileId == null ? null : Long.valueOf(fileId.toString()));
        return ApiResponse.ok();
    }

    @GetMapping("/students")
    public ApiResponse<PageResponse<UserMeResponse>> searchStudents(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer generation, @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer classNo, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(userQueryService.searchStudents(keyword, generation, region, classNo, page, size));
    }

    @GetMapping("/mentors")
    public ApiResponse<PageResponse<UserMeResponse>> getMentors(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String region, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(userQueryService.getMentors(keyword, region, page, size));
    }

    @GetMapping("/mentors/{mentorId}")
    public ApiResponse<UserMeResponse> getMentor(@PathVariable Long mentorId) {
        return ApiResponse.ok(userQueryService.getMentor(mentorId));
    }
}
