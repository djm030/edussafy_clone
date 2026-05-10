package com.edussafy.clone.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.edussafy.clone.domain.user.application.command.VerifyPasswordCommand;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void verifyPassword_returns_true_when_raw_password_matches_encoded_password() {
        User user = userWithPassword(passwordEncoder.encode("password1234!"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, passwordEncoder);

        boolean result = service.verifyPassword(1L, new VerifyPasswordCommand("password1234!"));

        assertThat(result).isTrue();
    }

    @Test
    void verifyPassword_returns_false_when_raw_password_does_not_match() {
        User user = userWithPassword(passwordEncoder.encode("password1234!"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, passwordEncoder);

        boolean result = service.verifyPassword(1L, new VerifyPasswordCommand("wrong-password"));

        assertThat(result).isFalse();
    }

    @Test
    void verifyPassword_throws_when_user_not_found() {
        given(userRepository.findById(999L)).willReturn(Optional.empty());
        UserCommandService service = new UserCommandService(userRepository, passwordEncoder);

        assertThatThrownBy(() -> service.verifyPassword(999L, new VerifyPasswordCommand("password")))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void changePassword_encodes_new_password_and_updates_user() {
        User user = userWithPassword(passwordEncoder.encode("old-password"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, passwordEncoder);

        service.changePassword(1L, "new-password123!");

        assertThat(passwordEncoder.matches("new-password123!", user.getPassword())).isTrue();
        verify(userRepository).findById(1L);
    }

    private User userWithPassword(String password) {
        return User.builder()
                .id(1L)
                .email("user@example.com")
                .password(password)
                .name("홍길동")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
