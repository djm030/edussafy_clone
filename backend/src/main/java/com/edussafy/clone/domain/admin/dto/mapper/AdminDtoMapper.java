package com.edussafy.clone.domain.admin.dto.mapper;

import com.edussafy.clone.domain.admin.domain.entity.AuditLog;
import com.edussafy.clone.domain.admin.dto.response.AdminUserResponse;
import com.edussafy.clone.domain.admin.dto.response.AuditLogResponse;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.entity.EducationCalendarDay;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceAppealResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceRecordResponse;
import com.edussafy.clone.domain.attendance.dto.response.EducationCalendarDayResponse;
import com.edussafy.clone.domain.point.domain.entity.PointTransaction;
import com.edussafy.clone.domain.point.dto.response.PointTransactionResponse;
import com.edussafy.clone.domain.user.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AdminDtoMapper {
    public AdminUserResponse toUserResponse(User user) {
        return new AdminUserResponse(user.getId(), user.getEmail(), user.getName(), user.getStudentNo(), user.getGeneration(),
                user.getRegion(), user.getClassNo(), user.getPhoneNumber(), user.getEmergencyPhoneNumber(), user.getZipCode(),
                user.getAddress(), user.getAddressDetail(), user.getRole(), user.getStatus(), user.getCreatedAt(), user.getUpdatedAt());
    }
    public AttendanceRecordResponse toAttendanceRecordResponse(AttendanceRecord record) {
        return new AttendanceRecordResponse(record.getId(), record.getCourse() == null ? null : record.getCourse().getId(),
                record.getCalendarDay() == null ? null : record.getCalendarDay().getId(), record.getAttendanceDate(),
                record.getCheckInAt(), record.getCheckOutAt(), record.getStatus(), record.getIssueTypes(), record.getReasonStatus(),
                record.getReasonText(), record.getCheckInType(), record.getCheckOutType(), record.getNote());
    }
    public AttendanceAppealResponse toAttendanceAppealResponse(AttendanceAppeal appeal) {
        return new AttendanceAppealResponse(appeal.getId(), appeal.getAttendanceRecord().getId(), appeal.getAppealType(),
                appeal.getReason(), appeal.getAttachmentFile() == null ? null : appeal.getAttachmentFile().getId(),
                appeal.getAppealStatus(), appeal.getReviewedBy() == null ? null : appeal.getReviewedBy().getId(), appeal.getReviewedAt(),
                appeal.getReviewComment(), appeal.getCreatedAt());
    }
    public EducationCalendarDayResponse toCalendarResponse(EducationCalendarDay day) {
        return new EducationCalendarDayResponse(day.getId(), day.getCourse() == null ? null : day.getCourse().getId(), day.getCalendarDate(),
                day.getDayType(), day.getIsEducationDay(), day.getTitle(), day.getDescription());
    }
    public PointTransactionResponse toPointTransactionResponse(PointTransaction tx) {
        return new PointTransactionResponse(tx.getId(), tx.getTransactionType(), tx.getPointAmount(), tx.getExpAmount(), tx.getReason(), tx.getTargetType(), tx.getTargetId(), tx.getCreatedAt());
    }
    public AuditLogResponse toAuditLogResponse(AuditLog log) {
        return new AuditLogResponse(log.getId(), log.getActor() == null ? null : log.getActor().getId(), log.getActor() == null ? null : log.getActor().getName(),
                log.getAction(), log.getTargetType(), log.getTargetId(), log.getMessage(), log.getIpAddress(), log.getCreatedAt());
    }
}
