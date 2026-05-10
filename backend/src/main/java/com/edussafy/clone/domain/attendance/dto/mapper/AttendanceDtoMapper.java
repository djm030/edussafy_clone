package com.edussafy.clone.domain.attendance.dto.mapper;

import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.entity.EducationCalendarDay;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceAppealResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceRecordResponse;
import com.edussafy.clone.domain.attendance.dto.response.EducationCalendarDayResponse;
import org.springframework.stereotype.Component;

@Component
public class AttendanceDtoMapper {
    public AttendanceRecordResponse toRecordResponse(AttendanceRecord record) {
        return new AttendanceRecordResponse(record.getId(), record.getCourse() == null ? null : record.getCourse().getId(),
                record.getCalendarDay() == null ? null : record.getCalendarDay().getId(), record.getAttendanceDate(),
                record.getCheckInAt(), record.getCheckOutAt(), record.getStatus(), record.getIssueTypes(),
                record.getReasonStatus(), record.getReasonText(), record.getCheckInType(), record.getCheckOutType(), record.getNote());
    }
    public AttendanceAppealResponse toAppealResponse(AttendanceAppeal appeal) {
        return new AttendanceAppealResponse(appeal.getId(), appeal.getAttendanceRecord().getId(), appeal.getAppealType(),
                appeal.getReason(), appeal.getAttachmentFile() == null ? null : appeal.getAttachmentFile().getId(),
                appeal.getAppealStatus(), appeal.getReviewedBy() == null ? null : appeal.getReviewedBy().getId(),
                appeal.getReviewedAt(), appeal.getReviewComment(), appeal.getCreatedAt());
    }
    public EducationCalendarDayResponse toCalendarResponse(EducationCalendarDay day) {
        return new EducationCalendarDayResponse(day.getId(), day.getCourse() == null ? null : day.getCourse().getId(),
                day.getCalendarDate(), day.getDayType(), day.getIsEducationDay(), day.getTitle(), day.getDescription());
    }
}
