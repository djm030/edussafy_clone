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
import com.edussafy.clone.global.file.FileResource;
import com.edussafy.clone.global.file.FileRole;
import com.edussafy.clone.global.file.FileResourceRepository;
import com.edussafy.clone.global.file.FileTargetType;
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

    @Mock
    private FileResourceRepository fileResourceRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void verifyPassword_returns_true_when_raw_password_matches_encoded_password() {
        User user = userWithPassword(passwordEncoder.encode("password1234!"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        boolean result = service.verifyPassword(1L, new VerifyPasswordCommand("password1234!"));

        assertThat(result).isTrue();
    }

    @Test
    void verifyPassword_returns_false_when_raw_password_does_not_match() {
        User user = userWithPassword(passwordEncoder.encode("password1234!"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        boolean result = service.verifyPassword(1L, new VerifyPasswordCommand("wrong-password"));

        assertThat(result).isFalse();
    }

    @Test
    void verifyPassword_throws_when_user_not_found() {
        given(userRepository.findById(999L)).willReturn(Optional.empty());
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        assertThatThrownBy(() -> service.verifyPassword(999L, new VerifyPasswordCommand("password")))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void changePassword_encodes_new_password_and_updates_user() {
        User user = userWithPassword(passwordEncoder.encode("old-password"));
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        service.changePassword(1L, "new-password123!");

        assertThat(passwordEncoder.matches("new-password123!", user.getPassword())).isTrue();
        verify(userRepository).findById(1L);
    }

    @Test
    void updateProfileImage_connects_user_profile_file_when_file_is_user_profile_image() {
        User user = userWithPassword(passwordEncoder.encode("password"));
        FileResource file = fileResource(FileTargetType.USER_PROFILE, FileRole.PROFILE_IMAGE);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(fileResourceRepository.findById(10L)).willReturn(Optional.of(file));
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        service.updateProfileImage(1L, 10L);

        assertThat(user.getProfileFile()).isSameAs(file);
    }

    @Test
    void updateProfileImage_clears_profile_file_when_file_id_is_null() {
        FileResource existing = fileResource(FileTargetType.USER_PROFILE, FileRole.PROFILE_IMAGE);
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded")
                .name("User")
                .profileFile(existing)
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        service.updateProfileImage(1L, null);

        assertThat(user.getProfileFile()).isNull();
    }

    @Test
    void updateProfileImage_rejects_non_profile_file() {
        User user = userWithPassword(passwordEncoder.encode("password"));
        FileResource file = fileResource(FileTargetType.BOARD_POST, FileRole.ATTACHMENT);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(fileResourceRepository.findById(10L)).willReturn(Optional.of(file));
        UserCommandService service = new UserCommandService(userRepository, fileResourceRepository, passwordEncoder);

        assertThatThrownBy(() -> service.updateProfileImage(1L, 10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("profile image file required");
    }

    private User userWithPassword(String password) {
        return User.builder()
                .id(1L)
                .email("user@example.com")
                .password(password)
                .name("User")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private FileResource fileResource(FileTargetType targetType, FileRole fileRole) {
        return FileResource.builder()
                .id(10L)
                .originalName("profile.png")
                .storedName("stored-profile.png")
                .fileUrl("/files/stored-profile.png")
                .targetType(targetType)
                .fileRole(fileRole)
                .build();
    }
}
