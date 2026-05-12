package com.edussafy.clone.domain.course.application;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseSession;
import com.edussafy.clone.domain.course.domain.entity.CourseWeek;
import com.edussafy.clone.domain.course.domain.enums.CourseSessionType;
import com.edussafy.clone.domain.course.domain.enums.CourseStatus;
import com.edussafy.clone.domain.course.domain.repository.CourseRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseSessionRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseWeekRepository;
import com.edussafy.clone.domain.course.dto.mapper.CourseDtoMapper;
import com.edussafy.clone.domain.course.dto.response.CourseCurriculumDayResponse;
import com.edussafy.clone.domain.course.dto.response.CourseCurriculumOverviewResponse;
import com.edussafy.clone.domain.course.dto.response.CourseCurriculumPhaseResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    public CourseCurriculumOverviewResponse getMyCurriculumOverview(Long userId, Long courseId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Course> courses = courseRepository.findByStatusOrderByStartDateDescIdDesc(CourseStatus.OPEN);
        Course course;
        if (courseId == null) {
            course = courses.stream()
                    .filter(candidate -> belongsToUser(candidate, user))
                    .findFirst()
                    .orElseThrow(CourseNotFoundException::new);
        } else {
            course = getCourseEntity(courseId);
            if (!belongsToUser(course, user)) {
                throw new CourseNotFoundException();
            }
        }
        List<CourseWeek> weeks = courseWeekRepository.findByCourseOrderBySortOrderAscWeekNoAsc(course);
        CourseWeek activeWeek = resolveActiveWeek(weeks);
        LocalDate startDate = activeWeek == null ? course.getStartDate() : activeWeek.getStartDate();
        LocalDate endDate = activeWeek == null ? course.getEndDate() : activeWeek.getEndDate();
        List<CourseSession> sessions = courseSessionRepository.findCourseSessionsInRange(course.getId(), startDate, endDate);

        return new CourseCurriculumOverviewResponse(
                courseDtoMapper.toCourseResponse(course),
                buildPhases(course),
                weeks.stream().map(courseDtoMapper::toWeekResponse).toList(),
                groupCurriculumDays(sessions)
        );
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

    private boolean belongsToUser(Course course, User user) {
        return (course.getGeneration() == null || course.getGeneration().equals(user.getGeneration()))
                && (course.getRegion() == null || course.getRegion().equals(user.getRegion()))
                && (course.getClassNo() == null || course.getClassNo().equals(user.getClassNo()));
    }

    private CourseWeek resolveActiveWeek(List<CourseWeek> weeks) {
        LocalDate today = LocalDate.now();
        return weeks.stream()
                .filter(week -> week.getStartDate() != null && week.getEndDate() != null)
                .filter(week -> !today.isBefore(week.getStartDate()) && !today.isAfter(week.getEndDate()))
                .findFirst()
                .orElseGet(() -> weeks.isEmpty() ? null : weeks.get(0));
    }

    private List<CourseCurriculumPhaseResponse> buildPhases(Course course) {
        LocalDate today = LocalDate.now();
        boolean active = course.getStartDate() == null || course.getEndDate() == null
                || (!today.isBefore(course.getStartDate()) && !today.isAfter(course.getEndDate()));
        String status;
        if (active) {
            status = "IN_PROGRESS";
        } else if (today.isBefore(course.getStartDate())) {
            status = "PLANNED";
        } else {
            status = "DONE";
        }
        return List.of(new CourseCurriculumPhaseResponse(course.getTitle(), status, active));
    }

    private List<CourseCurriculumDayResponse> groupCurriculumDays(List<CourseSession> sessions) {
        Map<LocalDate, List<CourseSession>> sessionsByDate = new LinkedHashMap<>();
        for (CourseSession session : sessions) {
            LocalDate date = session.getSessionDate();
            if (date == null && session.getStartAt() != null) date = session.getStartAt().toLocalDate();
            if (date == null) continue;
            sessionsByDate.computeIfAbsent(date, ignored -> new java.util.ArrayList<>()).add(session);
        }
        return sessionsByDate.entrySet().stream()
                .map(entry -> new CourseCurriculumDayResponse(
                        entry.getKey(),
                        resolveTimeRange(entry.getValue()),
                        null,
                        entry.getValue().stream().map(courseDtoMapper::toSessionResponse).toList()
                ))
                .toList();
    }

    private String resolveTimeRange(List<CourseSession> sessions) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return sessions.stream().filter(session -> session.getStartAt() != null).findFirst()
                .map(first -> {
                    String start = first.getStartAt().format(formatter);
                    String end = first.getEndAt() == null ? "" : first.getEndAt().format(formatter);
                    return end.isBlank() ? start : start + "~" + end;
                })
                .orElse("");
    }

    private String blankToNull(String keyword) {
        return keyword == null || keyword.isBlank() ? null : keyword;
    }
}
