package com.edussafy.clone.domain.attendance.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;

import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceAppealStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceIssueType;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceAppealRepository;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceRecordRepository;
import com.edussafy.clone.domain.attendance.domain.repository.EducationCalendarDayRepository;
import com.edussafy.clone.domain.attendance.dto.mapper.AttendanceDtoMapper;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceTodayResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceMonthlyResponse;
import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.file.FileResourceRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AttendanceServiceTest {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final LocalDate TODAY = LocalDate.of(2026, 5, 12);

    @Test
    void checkIn_marks_normal_between_0830_and_0900() {
        AttendanceService service = serviceAt("2026-05-11T23:40:00Z");
        User user = user();
        givenRepositories(user, Optional.empty());

        AttendanceTodayResponse response = service.checkIn(1L);

        assertThat(response.record().status()).isEqualTo(AttendanceStatus.NORMAL);
        assertThat(response.record().checkInAt().toLocalTime().toString()).isEqualTo("08:40");
        assertThat(response.canCheckIn()).isFalse();
    }

    @Test
    void checkIn_marks_late_after_0900() {
        AttendanceService service = serviceAt("2026-05-12T00:01:00Z");
        User user = user();
        givenRepositories(user, Optional.empty());

        AttendanceTodayResponse response = service.checkIn(1L);

        assertThat(response.record().status()).isEqualTo(AttendanceStatus.LATE);
    }

    @Test
    void checkIn_rejects_before_0830() {
        AttendanceService service = serviceAt("2026-05-11T23:29:00Z");
        User user = user();
        givenRepositories(user, Optional.empty());

        assertThatThrownBy(() -> service.checkIn(1L)).isInstanceOf(BusinessException.class);
    }

    @Test
    void checkOut_marks_early_leave_between_1400_and_1800() {
        AttendanceRecord record = checkedInRecord(AttendanceStatus.NORMAL);
        AttendanceService service = serviceAt("2026-05-12T06:00:00Z");
        User user = user();
        givenRepositories(user, Optional.of(record));

        AttendanceTodayResponse response = service.checkOut(1L);

        assertThat(response.record().status()).isEqualTo(AttendanceStatus.EARLY_LEAVE);
        assertThat(response.record().checkOutAt().toLocalTime().toString()).isEqualTo("15:00");
    }

    @Test
    void checkOut_after_1800_keeps_late_checkin_status() {
        AttendanceRecord record = checkedInRecord(AttendanceStatus.LATE);
        AttendanceService service = serviceAt("2026-05-12T09:00:00Z");
        User user = user();
        givenRepositories(user, Optional.of(record));

        AttendanceTodayResponse response = service.checkOut(1L);

        assertThat(response.record().status()).isEqualTo(AttendanceStatus.LATE);
        assertThat(response.record().checkOutAt().toLocalTime().toString()).isEqualTo("18:00");
    }

    @Test
    void checkOut_rejects_before_1400() {
        AttendanceRecord record = checkedInRecord(AttendanceStatus.NORMAL);
        AttendanceService service = serviceAt("2026-05-12T04:59:00Z");
        User user = user();
        givenRepositories(user, Optional.of(record));

        assertThatThrownBy(() -> service.checkOut(1L)).isInstanceOf(BusinessException.class);
    }

    @Test
    void getMonthly_filters_records_and_appeals_by_course() {
        AttendanceService service = serviceAt("2026-05-12T03:00:00Z");
        User user = user();
        AttendanceRecord lateRecord = AttendanceRecord.builder()
                .id(20L)
                .user(user)
                .course(Course.builder().id(3L).title("Java").build())
                .attendanceDate(LocalDate.of(2026, 5, 1))
                .checkInAt(LocalDate.of(2026, 5, 1).atTime(9, 10))
                .status(AttendanceStatus.LATE)
                .build();
        AttendanceAppeal appeal = AttendanceAppeal.builder()
                .id(30L)
                .attendanceRecord(lateRecord)
                .user(user)
                .appealType(AttendanceIssueType.LATE)
                .reason("Traffic")
                .appealStatus(AttendanceAppealStatus.SUBMITTED)
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(attendanceRecordRepository.findByUserAndAttendanceDate(user, TODAY)).willReturn(Optional.empty());
        given(attendanceRecordRepository.findByUserAndCourseIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                user, 3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31))).willReturn(List.of(lateRecord));
        given(attendanceAppealRepository.findByUserAndAttendanceRecordCourseIdAndAttendanceRecordAttendanceDateBetweenOrderByCreatedAtDesc(
                user, 3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31))).willReturn(List.of(appeal));
        given(educationCalendarDayRepository.findByCourseIdAndCalendarDateBetweenOrderByCalendarDateAsc(
                3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31))).willReturn(List.of());

        AttendanceMonthlyResponse response = service.getMonthly(1L, 2026, 5, 3L);

        assertThat(response.summary().lateCount()).isEqualTo(1);
        assertThat(response.summary().appealSubmittedCount()).isEqualTo(1);
        assertThat(response.days())
                .filteredOn(day -> LocalDate.of(2026, 5, 1).equals(day.date()))
                .singleElement()
                .satisfies(day -> {
                    assertThat(day.attendanceRecordId()).isEqualTo(20L);
                    assertThat(day.appealId()).isEqualTo(30L);
                    assertThat(day.canAppeal()).isFalse();
                });
        then(attendanceRecordRepository).should().findByUserAndCourseIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(
                user, 3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
        then(attendanceAppealRepository).should().findByUserAndAttendanceRecordCourseIdAndAttendanceRecordAttendanceDateBetweenOrderByCreatedAtDesc(
                user, 3L, LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31));
    }

    private final AttendanceRecordRepository attendanceRecordRepository = Mockito.mock(AttendanceRecordRepository.class);
    private final AttendanceAppealRepository attendanceAppealRepository = Mockito.mock(AttendanceAppealRepository.class);
    private final EducationCalendarDayRepository educationCalendarDayRepository = Mockito.mock(EducationCalendarDayRepository.class);
    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final FileResourceRepository fileResourceRepository = Mockito.mock(FileResourceRepository.class);

    private AttendanceService serviceAt(String instant) {
        Clock clock = Clock.fixed(Instant.parse(instant), SEOUL);
        return new AttendanceService(
                attendanceRecordRepository,
                attendanceAppealRepository,
                educationCalendarDayRepository,
                userRepository,
                fileResourceRepository,
                new AttendanceDtoMapper(),
                clock
        );
    }

    private void givenRepositories(User user, Optional<AttendanceRecord> todayRecord) {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(attendanceRecordRepository.findByUserAndAttendanceDate(user, TODAY)).willReturn(todayRecord);
        given(attendanceRecordRepository.save(any(AttendanceRecord.class))).willAnswer(invocation -> invocation.getArgument(0));
    }

    private AttendanceRecord checkedInRecord(AttendanceStatus status) {
        return AttendanceRecord.builder()
                .id(10L)
                .user(user())
                .attendanceDate(TODAY)
                .checkInAt(TODAY.atTime(9, 5))
                .status(status)
                .build();
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("student@example.com")
                .password("encoded")
                .name("Student")
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
