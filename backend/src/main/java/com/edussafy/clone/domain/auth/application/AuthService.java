package com.edussafy.clone.domain.auth.application;

import com.edussafy.clone.domain.auth.dto.request.LoginRequest;
import com.edussafy.clone.domain.auth.dto.response.LoginResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
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
}
