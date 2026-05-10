package com.edussafy.clone.domain.user.application;

import com.edussafy.clone.domain.user.application.command.UpdateUserProfileCommand;
import com.edussafy.clone.domain.user.application.command.VerifyPasswordCommand;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void updateProfile(Long userId, UpdateUserProfileCommand command) {
        User user = getUser(userId);
        user.updateProfile(command.phoneNumber(), command.emergencyPhoneNumber(), command.zipCode(), command.address(), command.addressDetail());
    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(Long userId, VerifyPasswordCommand command) {
        User user = getUser(userId);
        return passwordEncoder.matches(command.password(), user.getPassword());
    }

    public void changePassword(Long userId, String rawPassword) {
        User user = getUser(userId);
        user.changePassword(passwordEncoder.encode(rawPassword));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }
}
