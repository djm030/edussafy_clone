package com.edussafy.clone.domain.course.application;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseWeek;
import com.edussafy.clone.domain.course.domain.enums.CourseSessionType;
import com.edussafy.clone.domain.course.domain.enums.CourseStatus;
import com.edussafy.clone.domain.course.domain.repository.CourseRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseSessionRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseWeekRepository;
import com.edussafy.clone.domain.course.dto.mapper.CourseDtoMapper;
import com.edussafy.clone.domain.course.dto.response.CourseResponse;
import com.edussafy.clone.domain.course.dto.response.CourseSessionResponse;
import com.edussafy.clone.domain.course.dto.response.CourseWeekResponse;
import com.edussafy.clone.domain.course.exception.CourseNotFoundException;
import com.edussafy.clone.domain.course.exception.CourseSessionNotFoundException;
import com.edussafy.clone.domain.course.exception.CourseWeekNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseQueryService {
    private final CourseRepository courseRepository;
    private final CourseWeekRepository courseWeekRepository;
    private final CourseSessionRepository courseSessionRepository;
    private final UserRepository userRepository;
    private final CourseDtoMapper courseDtoMapper;

    public List<CourseResponse> getMyCourses(Long userId) {
        return courseRepository.findByStatusOrderByStartDateDescIdDesc(CourseStatus.OPEN).stream()
                .map(courseDtoMapper::toCourseResponse)
                .toList();
    }

    public CourseResponse getCourse(Long courseId) {
        return courseDtoMapper.toCourseResponse(getCourseEntity(courseId));
    }

    public List<CourseWeekResponse> getWeeks(Long courseId) {
        Course course = getCourseEntity(courseId);
        return courseWeekRepository.findByCourseOrderBySortOrderAscWeekNoAsc(course).stream()
                .map(courseDtoMapper::toWeekResponse)
                .toList();
    }

    public PageResponse<CourseSessionResponse> getWeekSessions(Long courseId, Long weekId, int page, int size) {
        Course course = getCourseEntity(courseId);
        CourseWeek week = courseWeekRepository.findById(weekId).orElseThrow(CourseWeekNotFoundException::new);
        Page<CourseSessionResponse> sessions = courseSessionRepository
                .findByCourseAndWeekOrderBySortOrderAscIdAsc(course, week, PageRequest.of(page, size))
                .map(courseDtoMapper::toSessionResponse);
        return PageResponse.from(sessions);
    }

    public CourseSessionResponse getSession(Long sessionId) {
        return courseSessionRepository.findById(sessionId)
                .map(courseDtoMapper::toSessionResponse)
                .orElseThrow(CourseSessionNotFoundException::new);
    }

    public PageResponse<CourseSessionResponse> getReplays(Long courseId, Long weekId, String keyword, int page, int size) {
        Page<CourseSessionResponse> sessions = courseSessionRepository
                .searchSessions(courseId, weekId, CourseSessionType.REPLAY, blankToNull(keyword), PageRequest.of(page, size))
                .map(courseDtoMapper::toSessionResponse);
        return PageResponse.from(sessions);
    }

    public PageResponse<CourseSessionResponse> getMyReplays(Long userId, String keyword, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return PageResponse.from(courseSessionRepository
                .findMyReplays(user.getGeneration(), user.getRegion(), user.getClassNo(), CourseSessionType.REPLAY, blankToNull(keyword), PageRequest.of(page, size))
                .map(courseDtoMapper::toSessionResponse));
    }

    public PageResponse<CourseSessionResponse> getCourseSessionsInRange(Long courseId, LocalDate startDate, LocalDate endDate, int page, int size) {
        getCourseEntity(courseId);
        return PageResponse.from(courseSessionRepository
                .findCourseSessionsInRange(courseId, startDate, endDate, PageRequest.of(page, size))
                .map(courseDtoMapper::toSessionResponse));
    }

    private Course getCourseEntity(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new);
    }

    private String blankToNull(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword;
    }
}
