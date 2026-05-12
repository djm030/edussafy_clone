package com.edussafy.clone.domain.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.edussafy.clone.domain.auth.dto.request.LoginRequest;
import com.edussafy.clone.domain.auth.dto.request.TokenRefreshRequest;
import com.edussafy.clone.domain.auth.dto.response.LoginResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.domain.repository.PasswordChangeHistoryRepository;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.global.exception.BusinessException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordChangeHistoryRepository passwordChangeHistoryRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDtoMapper userDtoMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_returns_token_and_user_when_password_matches() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded")
                .name("홍길동")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        UserMeResponse userResponse = new UserMeResponse(1L, "user@example.com", "홍길동", null, null, null, null, UserRole.STUDENT, UserStatus.ACTIVE);

        given(userRepository.findByEmail("user@example.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("raw", "encoded")).willReturn(true);
        given(jwtTokenProvider.createAccessToken(1L, "user@example.com", UserRole.STUDENT)).willReturn("access-token");
        given(jwtTokenProvider.createRefreshToken(1L)).willReturn("refresh-token");
        given(userDtoMapper.toMeResponse(user)).willReturn(userResponse);

        LoginResponse response = authService.login(new LoginRequest("user@example.com", "raw"));

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.user()).isEqualTo(userResponse);
    }

    @Test
    void login_throws_business_exception_when_password_does_not_match() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded")
                .name("홍길동")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();

        given(userRepository.findByEmail("user@example.com")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrong", "encoded")).willReturn(false);

        assertThatThrownBy(() -> authService.login(new LoginRequest("user@example.com", "wrong")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("이메일 또는 비밀번호가 올바르지 않습니다.");
    }

    @Test
    void refresh_returns_new_token_pair_when_refresh_token_is_valid() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded")
                .name("User")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        UserMeResponse userResponse = new UserMeResponse(1L, "user@example.com", "User", null, null, null, null, UserRole.STUDENT, UserStatus.ACTIVE);

        given(jwtTokenProvider.validateRefreshToken("refresh-token")).willReturn(true);
        given(jwtTokenProvider.getUserId("refresh-token")).willReturn(1L);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(jwtTokenProvider.createAccessToken(1L, "user@example.com", UserRole.STUDENT)).willReturn("new-access-token");
        given(jwtTokenProvider.createRefreshToken(1L)).willReturn("new-refresh-token");
        given(userDtoMapper.toMeResponse(user)).willReturn(userResponse);

        LoginResponse response = authService.refresh(new TokenRefreshRequest("refresh-token"));

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo("new-refresh-token");
        assertThat(response.user()).isEqualTo(userResponse);
    }

    @Test
    void refresh_throws_business_exception_when_refresh_token_is_invalid() {
        given(jwtTokenProvider.validateRefreshToken("invalid-token")).willReturn(false);

        assertThatThrownBy(() -> authService.refresh(new TokenRefreshRequest("invalid-token")))
                .isInstanceOf(BusinessException.class);
    }
}
