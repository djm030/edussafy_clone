package com.edussafy.clone.domain.admin.dto.request;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AdminUserCreateRequest(@Email @NotBlank String email, @NotBlank String name, String studentNo, Integer generation, String region, Integer classNo,
                                     String phoneNumber, String emergencyPhoneNumber, String zipCode, String address, String addressDetail,
                                     UserRole role, @NotBlank String initialPassword) {
}
