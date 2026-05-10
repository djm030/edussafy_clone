package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminAttendanceService;
import com.edussafy.clone.domain.admin.dto.request.*;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceAppealResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceRecordResponse;
import com.edussafy.clone.domain.attendance.dto.response.EducationCalendarDayResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminAttendanceController {
    private final AdminAttendanceService service;

    @GetMapping("/api/v1/admin/attendance")
    public ApiResponse<PageResponse<AttendanceRecordResponse>> getRecords(@CurrentUser Long adminId, @RequestParam(required = false) Long userId, @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) AttendanceStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getRecords(adminId, userId, courseId, startDate, endDate, status, page, size));
    }
    @PostMapping("/api/v1/admin/attendance")
    public ApiResponse<AttendanceRecordResponse> createRecord(@CurrentUser Long adminId, @Valid @RequestBody AdminAttendanceCreateRequest request) { return ApiResponse.ok(service.createRecord(adminId, request)); }
    @PatchMapping("/api/v1/admin/attendance/{attendanceRecordId}")
    public ApiResponse<AttendanceRecordResponse> updateRecord(@CurrentUser Long adminId, @PathVariable Long attendanceRecordId, @RequestBody AdminAttendanceUpdateRequest request) { return ApiResponse.ok(service.updateRecord(adminId, attendanceRecordId, request)); }
    @GetMapping("/api/v1/admin/attendance/appeals")
    public ApiResponse<PageResponse<AttendanceAppealResponse>> getAppeals(@CurrentUser Long adminId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) { return ApiResponse.ok(service.getAppeals(adminId, page, size)); }
    @PostMapping("/api/v1/admin/attendance/appeals/{appealId}/approve")
    public ApiResponse<AttendanceAppealResponse> approve(@CurrentUser Long adminId, @PathVariable Long appealId, @RequestBody AdminAppealReviewRequest request) { return ApiResponse.ok(service.approveAppeal(adminId, appealId, request)); }
    @PostMapping("/api/v1/admin/attendance/appeals/{appealId}/reject")
    public ApiResponse<AttendanceAppealResponse> reject(@CurrentUser Long adminId, @PathVariable Long appealId, @RequestBody AdminAppealReviewRequest request) { return ApiResponse.ok(service.rejectAppeal(adminId, appealId, request)); }
    @PostMapping("/api/v1/admin/education-calendar")
    public ApiResponse<EducationCalendarDayResponse> createCalendarDay(@CurrentUser Long adminId, @Valid @RequestBody AdminCalendarDayRequest request) { return ApiResponse.ok(service.createCalendarDay(adminId, request)); }
    @PatchMapping("/api/v1/admin/education-calendar/{calendarDayId}")
    public ApiResponse<EducationCalendarDayResponse> updateCalendarDay(@CurrentUser Long adminId, @PathVariable Long calendarDayId, @RequestBody AdminCalendarDayRequest request) { return ApiResponse.ok(service.updateCalendarDay(adminId, calendarDayId, request)); }
    @DeleteMapping("/api/v1/admin/education-calendar/{calendarDayId}")
    public ApiResponse<Void> deleteCalendarDay(@CurrentUser Long adminId, @PathVariable Long calendarDayId) { service.deleteCalendarDay(adminId, calendarDayId); return ApiResponse.ok(); }
}
