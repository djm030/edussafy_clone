package com.edussafy.clone.domain.auth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.user.domain.enums.UserRole;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    @Test
    void create_access_token_and_extract_subject_and_role() {
        JwtTokenProvider provider = new JwtTokenProvider(
                "1234567890123456789012345678901234567890123456789012345678901234",
                3600000L
        );

        String token = provider.createAccessToken(1L, "user@example.com", UserRole.STUDENT);

        assertThat(provider.validateToken(token)).isTrue();
        assertThat(provider.getUserId(token)).isEqualTo(1L);
        assertThat(provider.getEmail(token)).isEqualTo("user@example.com");
        assertThat(provider.getRole(token)).isEqualTo(UserRole.STUDENT);
    }
}
