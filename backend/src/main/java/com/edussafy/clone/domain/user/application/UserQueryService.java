package com.edussafy.clone.domain.user.application;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public UserMeResponse getMe(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return userDtoMapper.toMeResponse(user);
    }
}
