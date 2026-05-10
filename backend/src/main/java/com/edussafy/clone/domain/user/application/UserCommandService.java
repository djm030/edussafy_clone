package com.edussafy.clone.domain.user.application;

import com.edussafy.clone.domain.user.application.command.UpdateUserProfileCommand;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandService {

    private final UserRepository userRepository;

    public void updateProfile(Long userId, UpdateUserProfileCommand command) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.updateProfile(command.phoneNumber(), command.emergencyPhoneNumber(), command.zipCode(), command.address(), command.addressDetail());
    }
}
