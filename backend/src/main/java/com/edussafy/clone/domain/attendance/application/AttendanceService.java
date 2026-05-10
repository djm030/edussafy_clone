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
import com.edussafy.clone.domain.attendance.exception.AttendanceRecordNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.file.FileResource;
import com.edussafy.clone.global.file.FileResourceRepository;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    private final AttendanceRecordRepository attendanceRecordRepository;
    private final AttendanceAppealRepository attendanceAppealRepository;
    private final UserRepository userRepository;
    private final FileResourceRepository fileResourceRepository;
    private final AttendanceDtoMapper mapper;

    public PageResponse<AttendanceRecordResponse> getMyRecords(Long userId, LocalDate startDate, LocalDate endDate,
                                                               AttendanceStatus status, int page, int size) {
        User user = getUser(userId);
        LocalDate from = startDate == null ? LocalDate.now().minusMonths(1) : startDate;
        LocalDate to = endDate == null ? LocalDate.now() : endDate;
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

    private User getUser(Long userId) { return userRepository.findById(userId).orElseThrow(UserNotFoundException::new); }
}
