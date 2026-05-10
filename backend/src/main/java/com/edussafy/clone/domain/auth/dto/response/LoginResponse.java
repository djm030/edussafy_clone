package com.edussafy.clone.domain.auth.dto.response;

import com.edussafy.clone.domain.user.dto.response.UserMeResponse;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        UserMeResponse user
) {
}
