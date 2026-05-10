package com.edussafy.clone.domain.admin.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AdminPasswordResetRequest(@NotBlank String newPassword) {
}
