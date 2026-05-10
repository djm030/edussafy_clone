package com.edussafy.clone.domain.auth.application;

import com.edussafy.clone.domain.auth.dto.request.LoginRequest;
import com.edussafy.clone.domain.auth.dto.request.PasswordChangeRequest;
import com.edussafy.clone.domain.auth.dto.request.PasswordResetTemporaryRequest;
import com.edussafy.clone.domain.auth.dto.response.LoginResponse;
import com.edussafy.clone.domain.auth.dto.response.TemporaryPasswordResponse;
import com.edussafy.clone.domain.user.domain.entity.PasswordChangeHistory;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.PasswordChangeHistoryRepository;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private static final String TEMP_PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%";
    private static final int TEMP_PASSWORD_LENGTH = 12;

    private final UserRepository userRepository;
    private final PasswordChangeHistoryRepository passwordChangeHistoryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDtoMapper userDtoMapper;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException(ErrorCode.LOGIN_FAILED));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }

        return new LoginResponse(
                jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole()),
                jwtTokenProvider.createRefreshToken(user.getId()),
                userDtoMapper.toMeResponse(user)
        );
    }

    @Transactional
    public TemporaryPasswordResponse resetTemporaryPassword(PasswordResetTemporaryRequest request) {
        User user = userRepository.findByEmailAndName(request.email(), request.name())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        String temporaryPassword = generateTemporaryPassword();
        user.changePassword(passwordEncoder.encode(temporaryPassword));
        passwordChangeHistoryRepository.save(PasswordChangeHistory.builder()
                .user(user)
                .changedAt(LocalDateTime.now())
                .build());
        return new TemporaryPasswordResponse(temporaryPassword);
    }

    @Transactional
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
        user.changePassword(passwordEncoder.encode(request.newPassword()));
        passwordChangeHistoryRepository.save(PasswordChangeHistory.builder()
                .user(user)
                .changedAt(LocalDateTime.now())
                .build());
    }

    private String generateTemporaryPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            password.append(TEMP_PASSWORD_CHARS.charAt(random.nextInt(TEMP_PASSWORD_CHARS.length())));
        }
        return password.toString();
    }
}
