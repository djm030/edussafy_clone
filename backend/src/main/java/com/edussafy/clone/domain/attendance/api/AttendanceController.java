package com.edussafy.clone.domain.attendance.api;

import com.edussafy.clone.domain.attendance.application.AttendanceService;
import com.edussafy.clone.domain.attendance.application.EducationCalendarService;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.attendance.dto.request.AttendanceAppealCreateRequest;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceAppealResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceRecordResponse;
import com.edussafy.clone.domain.attendance.dto.response.AttendanceTodayResponse;
import com.edussafy.clone.domain.attendance.dto.response.EducationCalendarDayResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final EducationCalendarService educationCalendarService;

    @GetMapping("/attendance/my")
    public ApiResponse<PageResponse<AttendanceRecordResponse>> getMyAttendance(
            @CurrentUser Long currentUserId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) AttendanceStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(attendanceService.getMyRecords(currentUserId, startDate, endDate, status, page, size));
    }

    @GetMapping("/attendance/my/{attendanceRecordId}")
    public ApiResponse<AttendanceRecordResponse> getMyAttendanceDetail(@CurrentUser Long currentUserId, @PathVariable Long attendanceRecordId) {
        return ApiResponse.ok(attendanceService.getMyRecord(currentUserId, attendanceRecordId));
    }

    @GetMapping("/attendance/today")
    public ApiResponse<AttendanceTodayResponse> getTodayAttendance(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(attendanceService.getToday(currentUserId));
    }

    @PostMapping("/attendance/check-in")
    public ApiResponse<AttendanceTodayResponse> checkIn(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(attendanceService.checkIn(currentUserId));
    }

    @PostMapping("/attendance/check-out")
    public ApiResponse<AttendanceTodayResponse> checkOut(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(attendanceService.checkOut(currentUserId));
    }

    @PostMapping("/attendance/appeals")
    public ApiResponse<AttendanceAppealResponse> createAppeal(@CurrentUser Long currentUserId, @Valid @RequestBody AttendanceAppealCreateRequest request) {
        return ApiResponse.ok(attendanceService.createAppeal(currentUserId, request));
    }

    @GetMapping("/attendance/appeals/my")
    public ApiResponse<PageResponse<AttendanceAppealResponse>> getMyAppeals(@CurrentUser Long currentUserId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(attendanceService.getMyAppeals(currentUserId, page, size));
    }

    @GetMapping("/education-calendar")
    public ApiResponse<List<EducationCalendarDayResponse>> getCalendar(@RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) {
        return ApiResponse.ok(educationCalendarService.getCalendar(courseId, year, month));
    }
}
