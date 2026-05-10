package com.edussafy.clone.domain.activity.application;

import com.edussafy.clone.domain.activity.domain.enums.ActivityType;
import com.edussafy.clone.domain.activity.domain.repository.UserActivityRecordRepository;
import com.edussafy.clone.domain.activity.dto.mapper.ActivityDtoMapper;
import com.edussafy.clone.domain.activity.dto.response.UserActivityRecordResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ActivityService {
    private final UserActivityRecordRepository repository;
    private final UserRepository userRepository;
    private final ActivityDtoMapper mapper;
    public PageResponse<UserActivityRecordResponse> getMyActivities(Long userId, ActivityType activityType, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return PageResponse.from((activityType == null
                ? repository.findByUserOrderByActivityDateDescIdDesc(user, PageRequest.of(page, size))
                : repository.findByUserAndActivityTypeOrderByActivityDateDescIdDesc(user, activityType, PageRequest.of(page, size)))
                .map(mapper::toResponse));
    }
}
