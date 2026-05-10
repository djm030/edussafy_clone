package com.edussafy.clone.domain.admin.dto.response;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import java.time.LocalDateTime;

public record AdminUserResponse(Long id, String email, String name, String studentNo, Integer generation, String region, Integer classNo,
                                String phoneNumber, String emergencyPhoneNumber, String zipCode, String address, String addressDetail,
                                UserRole role, UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
