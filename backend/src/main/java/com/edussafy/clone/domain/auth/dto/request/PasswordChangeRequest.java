package com.edussafy.clone.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PasswordChangeRequest(
        @NotBlank(message = "현재 비밀번호를 입력하세요.") String currentPassword,
        @NotBlank(message = "새 비밀번호를 입력하세요.") String newPassword
) {
}
