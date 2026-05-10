package com.edussafy.clone.domain.user.dto.response;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;

public record UserMeResponse(
        Long id,
        String email,
        String name,
        String studentNo,
        Integer generation,
        String region,
        Integer classNo,
        UserRole role,
        UserStatus status
) {
}
