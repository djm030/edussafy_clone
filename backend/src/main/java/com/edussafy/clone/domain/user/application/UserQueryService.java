package com.edussafy.clone.domain.user.application;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
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
    private final UserStatRepository userStatRepository;
    private final UserDtoMapper userDtoMapper;

    public UserMeResponse getMe(Long userId) {
        User user = getUser(userId);
        return userDtoMapper.toMeResponse(user);
    }

    public CampusSummaryResponse getCampusSummary(Long userId) {
        User user = getUser(userId);
        UserMeResponse userResponse = userDtoMapper.toMeResponse(user);
        return userStatRepository.findByUser(user)
                .map(stat -> toCampusSummary(userResponse, stat))
                .orElseGet(() -> defaultCampusSummary(userResponse));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private CampusSummaryResponse toCampusSummary(UserMeResponse userResponse, UserStat stat) {
        return new CampusSummaryResponse(
                userResponse,
                stat.getScholarshipPoint(),
                stat.getTotalExp(),
                stat.getLevelName(),
                stat.getLevelNo(),
                stat.getAttendanceRate(),
                stat.getCompletedLearningCount(),
                0L
        );
    }

    private CampusSummaryResponse defaultCampusSummary(UserMeResponse userResponse) {
        return new CampusSummaryResponse(userResponse, 0, 0, "Lv.1", 1, 0.0, 0, 0L);
    }
}
