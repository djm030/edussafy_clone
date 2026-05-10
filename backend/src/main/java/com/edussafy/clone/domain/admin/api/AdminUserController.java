package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminUserService;
import com.edussafy.clone.domain.admin.dto.request.AdminPasswordResetRequest;
import com.edussafy.clone.domain.admin.dto.request.AdminUserCreateRequest;
import com.edussafy.clone.domain.admin.dto.request.AdminUserUpdateRequest;
import com.edussafy.clone.domain.admin.dto.response.AdminUserImportResponse;
import com.edussafy.clone.domain.admin.dto.response.AdminUserResponse;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {
    private final AdminUserService service;

    @GetMapping
    public ApiResponse<PageResponse<AdminUserResponse>> getUsers(@CurrentUser Long adminId, @RequestParam(required = false) String keyword, @RequestParam(required = false) UserRole role,
            @RequestParam(required = false) UserStatus status, @RequestParam(required = false) Integer generation, @RequestParam(required = false) String region,
            @RequestParam(required = false) Integer classNo, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getUsers(adminId, keyword, role, status, generation, region, classNo, page, size));
    }
    @GetMapping("/{userId}")
    public ApiResponse<AdminUserResponse> getUser(@CurrentUser Long adminId, @PathVariable Long userId) { return ApiResponse.ok(service.getUser(adminId, userId)); }
    @PostMapping
    public ApiResponse<AdminUserResponse> createUser(@CurrentUser Long adminId, @Valid @RequestBody AdminUserCreateRequest request) { return ApiResponse.ok(service.createUser(adminId, request)); }
    @PostMapping("/import")
    public ApiResponse<AdminUserImportResponse> importUsers(@CurrentUser Long adminId, @RequestPart MultipartFile file, @RequestParam String defaultPassword, @RequestParam(defaultValue = "STUDENT") UserRole role) {
        return ApiResponse.ok(service.importUsers(adminId, file, defaultPassword, role));
    }
    @PatchMapping("/{userId}")
    public ApiResponse<AdminUserResponse> updateUser(@CurrentUser Long adminId, @PathVariable Long userId, @RequestBody AdminUserUpdateRequest request) { return ApiResponse.ok(service.updateUser(adminId, userId, request)); }
    @PostMapping("/{userId}/reset-password")
    public ApiResponse<Void> resetPassword(@CurrentUser Long adminId, @PathVariable Long userId, @Valid @RequestBody AdminPasswordResetRequest request) { service.resetPassword(adminId, userId, request); return ApiResponse.ok(); }
}
