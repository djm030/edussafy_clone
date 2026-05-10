package com.edussafy.clone.domain.user.application;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
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

    public PageResponse<UserMeResponse> searchStudents(String keyword, Integer generation, String region, Integer classNo, int page, int size) {
        Specification<User> spec = Specification.where((root, q, cb) -> cb.equal(root.get("role"), UserRole.STUDENT));
        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword + "%";
            spec = spec.and((root, q, cb) -> cb.or(cb.like(root.get("name"), like), cb.like(root.get("email"), like), cb.like(root.get("studentNo"), like)));
        }
        if (generation != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("generation"), generation));
        if (region != null && !region.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("region"), region));
        if (classNo != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("classNo"), classNo));
        return PageResponse.from(userRepository.findAll(spec, PageRequest.of(page, size)).map(userDtoMapper::toMeResponse));
    }

    public PageResponse<UserMeResponse> getMentors(String keyword, String region, int page, int size) {
        Specification<User> spec = Specification.where((root, q, cb) -> cb.equal(root.get("role"), UserRole.MENTOR));
        if (keyword != null && !keyword.isBlank()) {
            String like = "%" + keyword + "%";
            spec = spec.and((root, q, cb) -> cb.or(cb.like(root.get("name"), like), cb.like(root.get("email"), like)));
        }
        if (region != null && !region.isBlank()) spec = spec.and((root, q, cb) -> cb.equal(root.get("region"), region));
        return PageResponse.from(userRepository.findAll(spec, PageRequest.of(page, size)).map(userDtoMapper::toMeResponse));
    }

    public UserMeResponse getMentor(Long mentorId) {
        User mentor = userRepository.findById(mentorId)
                .filter(user -> user.getRole() == UserRole.MENTOR)
                .orElseThrow(UserNotFoundException::new);
        return userDtoMapper.toMeResponse(mentor);
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
