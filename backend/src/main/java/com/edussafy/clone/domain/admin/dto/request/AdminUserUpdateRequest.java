package com.edussafy.clone.domain.admin.dto.request;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;

public record AdminUserUpdateRequest(String name, Integer generation, String region, Integer classNo, String phoneNumber, String emergencyPhoneNumber,
                                     String zipCode, String address, String addressDetail, UserRole role, UserStatus status) {
}
