package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAccessService {
    private final UserRepository userRepository;

    public User requireAdmin(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getRole() != UserRole.ADMIN && user.getRole() != UserRole.OPERATOR) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }
        return user;
    }
}
