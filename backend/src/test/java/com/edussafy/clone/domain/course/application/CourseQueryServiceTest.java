package com.edussafy.clone.domain.course.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseSession;
import com.edussafy.clone.domain.course.domain.entity.CourseWeek;
import com.edussafy.clone.domain.course.domain.enums.CourseSessionType;
import com.edussafy.clone.domain.course.domain.enums.CourseStatus;
import com.edussafy.clone.domain.course.domain.repository.CourseRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseSessionRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseWeekRepository;
import com.edussafy.clone.domain.course.dto.mapper.CourseDtoMapper;
import com.edussafy.clone.domain.course.dto.response.CourseCurriculumOverviewResponse;
import com.edussafy.clone.domain.course.exception.CourseNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CourseQueryServiceTest {
    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final CourseWeekRepository courseWeekRepository = Mockito.mock(CourseWeekRepository.class);
    private final CourseSessionRepository courseSessionRepository = Mockito.mock(CourseSessionRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final CourseQueryService service = new CourseQueryService(
            courseRepository,
            courseWeekRepository,
            courseSessionRepository,
            userRepository,
            new CourseDtoMapper()
    );

    @Test
    void getMyCurriculumOverview_returns_only_user_course_and_groups_sessions() {
        User user = user();
        Course course = course(1L, 12, "Seoul", 1);
        Course other = course(2L, 13, "Busan", 2);
        CourseWeek week = CourseWeek.builder()
                .id(10L)
                .course(course)
                .weekNo(1)
                .title("Week 1")
                .startDate(LocalDate.of(2026, 5, 11))
                .endDate(LocalDate.of(2026, 5, 17))
                .sortOrder(1)
                .build();
        CourseSession session = CourseSession.builder()
                .id(20L)
                .course(course)
                .week(week)
                .title("Spring")
                .sessionType(CourseSessionType.LIVE)
                .sessionDate(LocalDate.of(2026, 5, 12))
                .startAt(LocalDateTime.of(2026, 5, 12, 9, 0))
                .endAt(LocalDateTime.of(2026, 5, 12, 18, 0))
                .sortOrder(1)
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(courseRepository.findByStatusOrderByStartDateDescIdDesc(CourseStatus.OPEN)).willReturn(List.of(other, course));
        given(courseWeekRepository.findByCourseOrderBySortOrderAscWeekNoAsc(course)).willReturn(List.of(week));
        given(courseSessionRepository.findCourseSessionsInRange(course.getId(), week.getStartDate(), week.getEndDate())).willReturn(List.of(session));

        CourseCurriculumOverviewResponse response = service.getMyCurriculumOverview(1L, null);

        assertThat(response.course().id()).isEqualTo(1L);
        assertThat(response.weeks()).hasSize(1);
        assertThat(response.days()).hasSize(1);
        assertThat(response.days().get(0).timeRange()).isEqualTo("09:00~18:00");
        assertThat(response.days().get(0).sessions()).singleElement().satisfies(item -> assertThat(item.id()).isEqualTo(20L));
    }

    @Test
    void getMyCurriculumOverview_rejects_explicit_course_outside_user_scope() {
        User user = user();
        Course other = course(2L, 13, "Busan", 2);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(courseRepository.findById(2L)).willReturn(Optional.of(other));

        assertThatThrownBy(() -> service.getMyCurriculumOverview(1L, 2L))
                .isInstanceOf(CourseNotFoundException.class);
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("student@example.com")
                .password("encoded")
                .name("Student")
                .generation(12)
                .region("Seoul")
                .classNo(1)
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }

    private Course course(Long id, Integer generation, String region, Integer classNo) {
        return Course.builder()
                .id(id)
                .title("Course " + id)
                .generation(generation)
                .region(region)
                .classNo(classNo)
                .status(CourseStatus.OPEN)
                .startDate(LocalDate.of(2026, 5, 1))
                .endDate(LocalDate.of(2026, 5, 31))
                .build();
    }
}
