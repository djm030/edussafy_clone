package com.edussafy.clone.domain.admin.application;

import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.admin.dto.mapper.AdminDtoMapper;
import com.edussafy.clone.domain.admin.dto.request.*;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.entity.EducationCalendarDay;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceAppealStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceAppealRepository;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceRecordRepository;
import com.edussafy.clone.domain.attendance.domain.repository.EducationCalendarDayRepository;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceAppealResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceRecordResponse;
import com.edussafy.clone.domain.attendance.dto.response.EducationCalendarDayResponse;
import com.edussafy.clone.domain.attendance.exception.AttendanceAppealNotFoundException;
import com.edussafy.clone.domain.attendance.exception.AttendanceRecordNotFoundException;
import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.repository.CourseRepository;
import com.edussafy.clone.domain.course.exception.CourseNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminAttendanceService {
    private final AttendanceRecordRepository recordRepository;
    private final AttendanceAppealRepository appealRepository;
    private final EducationCalendarDayRepository calendarRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final AuditLogService auditLogService;
    private final AdminAccessService adminAccessService;
    private final AdminDtoMapper mapper;

    @Transactional(readOnly = true)
    public PageResponse<AttendanceRecordResponse> getRecords(Long adminId, Long userId, Long courseId, LocalDate startDate, LocalDate endDate, AttendanceStatus status, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        Specification<AttendanceRecord> spec = Specification.where(null);
        if (userId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("user").get("id"), userId));
        if (courseId != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("course").get("id"), courseId));
        if (startDate != null) spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("attendanceDate"), startDate));
        if (endDate != null) spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("attendanceDate"), endDate));
        if (status != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("status"), status));
        return PageResponse.from(recordRepository.findAll(spec, PageRequest.of(page, size)).map(mapper::toAttendanceRecordResponse));
    }

    @Transactional
    public AttendanceRecordResponse createRecord(Long adminId, AdminAttendanceCreateRequest request) {
        adminAccessService.requireAdmin(adminId);
        User user = userRepository.findById(request.userId()).orElseThrow(UserNotFoundException::new);
        Course course = request.courseId() == null ? null : courseRepository.findById(request.courseId()).orElseThrow(CourseNotFoundException::new);
        EducationCalendarDay day = request.calendarDayId() == null ? null : calendarRepository.findById(request.calendarDayId()).orElseThrow(AttendanceRecordNotFoundException::new);
        AttendanceRecord saved = recordRepository.save(AttendanceRecord.builder().user(user).course(course).calendarDay(day).attendanceDate(request.attendanceDate())
                .checkInAt(request.checkInAt()).checkOutAt(request.checkOutAt()).status(request.status()).issueTypes(request.issueTypes())
                .reasonStatus(request.reasonStatus()).reasonText(request.reasonText()).checkInType(request.checkInType()).checkOutType(request.checkOutType()).note(request.note()).build());
        auditLogService.record(adminId, AuditAction.CREATE, "ATTENDANCE_RECORD", saved.getId(), "출결 수동 등록");
        return mapper.toAttendanceRecordResponse(saved);
    }

    @Transactional
    public AttendanceRecordResponse updateRecord(Long adminId, Long recordId, AdminAttendanceUpdateRequest request) {
        adminAccessService.requireAdmin(adminId);
        AttendanceRecord record = recordRepository.findById(recordId).orElseThrow(AttendanceRecordNotFoundException::new);
        record.updateByAdmin(request.checkInAt(), request.checkOutAt(), request.status(), request.reasonStatus(), request.issueTypes(), request.reasonText(), request.checkInType(), request.checkOutType(), request.note());
        auditLogService.record(adminId, AuditAction.UPDATE, "ATTENDANCE_RECORD", recordId, "출결 수동 수정");
        return mapper.toAttendanceRecordResponse(record);
    }

    @Transactional(readOnly = true)
    public PageResponse<AttendanceAppealResponse> getAppeals(Long adminId, int page, int size) {
        adminAccessService.requireAdmin(adminId);
        return PageResponse.from(appealRepository.findAll(PageRequest.of(page, size)).map(mapper::toAttendanceAppealResponse));
    }

    @Transactional
    public AttendanceAppealResponse approveAppeal(Long adminId, Long appealId, AdminAppealReviewRequest request) {
        adminAccessService.requireAdmin(adminId);
        AttendanceAppeal appeal = appealRepository.findById(appealId).orElseThrow(AttendanceAppealNotFoundException::new);
        User reviewer = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);
        appeal.review(AttendanceAppealStatus.APPROVED, reviewer, request.reviewComment());
        auditLogService.record(adminId, AuditAction.APPROVE, "ATTENDANCE_APPEAL", appealId, "출결 소명 승인");
        return mapper.toAttendanceAppealResponse(appeal);
    }

    @Transactional
    public AttendanceAppealResponse rejectAppeal(Long adminId, Long appealId, AdminAppealReviewRequest request) {
        adminAccessService.requireAdmin(adminId);
        AttendanceAppeal appeal = appealRepository.findById(appealId).orElseThrow(AttendanceAppealNotFoundException::new);
        User reviewer = userRepository.findById(adminId).orElseThrow(UserNotFoundException::new);
        appeal.review(AttendanceAppealStatus.REJECTED, reviewer, request.reviewComment());
        auditLogService.record(adminId, AuditAction.REJECT, "ATTENDANCE_APPEAL", appealId, "출결 소명 반려");
        return mapper.toAttendanceAppealResponse(appeal);
    }

    @Transactional
    public EducationCalendarDayResponse createCalendarDay(Long adminId, AdminCalendarDayRequest request) {
        adminAccessService.requireAdmin(adminId);
        Course course = request.courseId() == null ? null : courseRepository.findById(request.courseId()).orElseThrow(CourseNotFoundException::new);
        EducationCalendarDay saved = calendarRepository.save(EducationCalendarDay.builder().course(course).calendarDate(request.calendarDate()).dayType(request.dayType()).isEducationDay(request.isEducationDay()).title(request.title()).description(request.description()).build());
        auditLogService.record(adminId, AuditAction.CREATE, "EDUCATION_CALENDAR", saved.getId(), "교육 달력 등록");
        return mapper.toCalendarResponse(saved);
    }
    @Transactional
    public EducationCalendarDayResponse updateCalendarDay(Long adminId, Long calendarDayId, AdminCalendarDayRequest request) {
        adminAccessService.requireAdmin(adminId);
        EducationCalendarDay day = calendarRepository.findById(calendarDayId).orElseThrow(AttendanceRecordNotFoundException::new);
        Course course = request.courseId() == null ? null : courseRepository.findById(request.courseId()).orElseThrow(CourseNotFoundException::new);
        day.update(course, request.calendarDate(), request.dayType(), request.isEducationDay(), request.title(), request.description());
        auditLogService.record(adminId, AuditAction.UPDATE, "EDUCATION_CALENDAR", calendarDayId, "교육 달력 수정");
        return mapper.toCalendarResponse(day);
    }
    @Transactional
    public void deleteCalendarDay(Long adminId, Long calendarDayId) {
        adminAccessService.requireAdmin(adminId);
        calendarRepository.deleteById(calendarDayId);
        auditLogService.record(adminId, AuditAction.DELETE, "EDUCATION_CALENDAR", calendarDayId, "교육 달력 삭제");
    }
}
