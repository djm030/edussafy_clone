package com.edussafy.clone.domain.attendance.application;

import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceAppealRepository;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceRecordRepository;
import com.edussafy.clone.domain.attendance.dto.mapper.AttendanceDtoMapper;
import com.edussafy.clone.domain.attendance.dto.request.AttendanceAppealCreateRequest;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceAppealResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceRecordResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceTodayResponse;
import com.edussafy.clone.domain.attendance.exception.AttendanceRecordNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.exception.BusinessException;
import com.edussafy.clone.global.exception.ErrorCode;
import com.edussafy.clone.global.file.FileResource;
import com.edussafy.clone.global.file.FileResourceRepository;
import com.edussafy.clone.global.response.PageResponse;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    private static final LocalTime CHECK_IN_START = LocalTime.of(8, 30);
    private static final LocalTime NORMAL_CHECK_IN_END = LocalTime.of(9, 0);
    private static final LocalTime CHECK_OUT_START = LocalTime.of(14, 0);
    private static final LocalTime NORMAL_CHECK_OUT_START = LocalTime.of(18, 0);
    private static final String ACTION_CHECK_IN = "CHECK_IN";
    private static final String ACTION_CHECK_OUT = "CHECK_OUT";
    private static final String ACTION_DONE = "DONE";
    private static final String ACTION_WAITING = "WAITING";
    private static final String WEB_ACTION_TYPE = "WEB";

    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceAppealRepository attendanceAppealRepository;
    private final UserRepository userRepository;
    private final FileResourceRepository fileResourceRepository;
    private final AttendanceDtoMapper mapper;
    private final Clock clock;

    public PageResponse<AttendanceRecordResponse> getMyRecords(Long userId, LocalDate startDate, LocalDate endDate,
                                                               AttendanceStatus status, int page, int size) {
        User user = getUser(userId);
        LocalDate from = startDate == null ? today().minusMonths(1) : startDate;
        LocalDate to = endDate == null ? today() : endDate;
        Page<AttendanceRecord> records = status == null
                ? attendanceRecordRepository.findByUserAndAttendanceDateBetweenOrderByAttendanceDateDesc(user, from, to, PageRequest.of(page, size))
                : attendanceRecordRepository.findByUserAndStatusAndAttendanceDateBetweenOrderByAttendanceDateDesc(user, status, from, to, PageRequest.of(page, size));
        return PageResponse.from(records.map(mapper::toRecordResponse));
    }

    public AttendanceRecordResponse getMyRecord(Long userId, Long recordId) {
        AttendanceRecord record = attendanceRecordRepository.findById(recordId).orElseThrow(AttendanceRecordNotFoundException::new);
        if (!record.getUser().getId().equals(userId)) throw new AttendanceRecordNotFoundException();
        return mapper.toRecordResponse(record);
    }

    public AttendanceTodayResponse getToday(Long userId) {
        User user = getUser(userId);
        LocalDate today = today();
        AttendanceRecord record = attendanceRecordRepository.findByUserAndAttendanceDate(user, today).orElse(null);
        return toTodayResponse(record, "오늘 출석 상태를 확인했습니다.");
    }

    @Transactional
    public AttendanceTodayResponse checkIn(Long userId) {
        User user = getUser(userId);
        LocalDateTime now = now();
        LocalTime time = now.toLocalTime();
        if (time.isBefore(CHECK_IN_START)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        AttendanceRecord record = attendanceRecordRepository.findByUserAndAttendanceDate(user, now.toLocalDate())
                .orElseGet(() -> attendanceRecordRepository.save(AttendanceRecord.builder()
                        .user(user)
                        .attendanceDate(now.toLocalDate())
                        .build()));
        if (record.getCheckInAt() != null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        AttendanceStatus status = time.isAfter(NORMAL_CHECK_IN_END) ? AttendanceStatus.LATE : AttendanceStatus.NORMAL;
        String note = status == AttendanceStatus.NORMAL ? "Normal check-in" : "Late check-in";
        record.checkIn(now, status, WEB_ACTION_TYPE, note);
        return toTodayResponse(record, status == AttendanceStatus.NORMAL ? "정상 출석 처리되었습니다." : "지각으로 출석 처리되었습니다.");
    }

    @Transactional
    public AttendanceTodayResponse checkOut(Long userId) {
        User user = getUser(userId);
        LocalDateTime now = now();
        LocalTime time = now.toLocalTime();
        AttendanceRecord record = attendanceRecordRepository.findByUserAndAttendanceDate(user, now.toLocalDate())
                .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_REQUEST));
        if (record.getCheckInAt() == null || record.getCheckOutAt() != null || time.isBefore(CHECK_OUT_START)) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        boolean earlyLeave = time.isBefore(NORMAL_CHECK_OUT_START);
        AttendanceStatus status = earlyLeave ? AttendanceStatus.EARLY_LEAVE : keepCheckInIssueOrNormal(record);
        String message = earlyLeave ? "조퇴로 퇴실 처리되었습니다." : "정상 퇴실 처리되었습니다.";
        record.checkOut(now, status, WEB_ACTION_TYPE, message);
        return toTodayResponse(record, message);
    }

    @Transactional
    public AttendanceAppealResponse createAppeal(Long userId, AttendanceAppealCreateRequest request) {
        User user = getUser(userId);
        AttendanceRecord record = attendanceRecordRepository.findById(request.attendanceRecordId()).orElseThrow(AttendanceRecordNotFoundException::new);
        if (!record.getUser().getId().equals(userId)) throw new AttendanceRecordNotFoundException();
        FileResource attachment = request.attachmentFileId() == null ? null : fileResourceRepository.findById(request.attachmentFileId()).orElse(null);
        AttendanceAppeal appeal = AttendanceAppeal.builder()
                .attendanceRecord(record).user(user).appealType(request.appealType()).reason(request.reason()).attachmentFile(attachment)
                .build();
        return mapper.toAppealResponse(attendanceAppealRepository.save(appeal));
    }

    public PageResponse<AttendanceAppealResponse> getMyAppeals(Long userId, int page, int size) {
        User user = getUser(userId);
        return PageResponse.from(attendanceAppealRepository.findByUserOrderByCreatedAtDesc(user, PageRequest.of(page, size)).map(mapper::toAppealResponse));
    }

    private AttendanceStatus keepCheckInIssueOrNormal(AttendanceRecord record) {
        return record.getStatus() == AttendanceStatus.LATE ? AttendanceStatus.LATE : AttendanceStatus.NORMAL;
    }

    private AttendanceTodayResponse toTodayResponse(AttendanceRecord record, String message) {
        LocalDateTime now = now();
        LocalTime time = now.toLocalTime();
        boolean canCheckIn = record == null || record.getCheckInAt() == null;
        canCheckIn = canCheckIn && !time.isBefore(CHECK_IN_START);
        boolean canCheckOut = record != null
                && record.getCheckInAt() != null
                && record.getCheckOutAt() == null
                && !time.isBefore(CHECK_OUT_START);
        String nextAction = resolveNextAction(record, time);
        return new AttendanceTodayResponse(
                record == null ? null : mapper.toRecordResponse(record),
                now.toLocalDate(),
                time,
                canCheckIn,
                canCheckOut,
                nextAction,
                message
        );
    }

    private String resolveNextAction(AttendanceRecord record, LocalTime time) {
        if (record == null || record.getCheckInAt() == null) {
            return time.isBefore(CHECK_IN_START) ? ACTION_WAITING : ACTION_CHECK_IN;
        }
        if (record.getCheckOutAt() != null) return ACTION_DONE;
        return time.isBefore(CHECK_OUT_START) ? ACTION_WAITING : ACTION_CHECK_OUT;
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }

    private LocalDateTime now() {
        return LocalDateTime.now(clock);
    }

    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
}
