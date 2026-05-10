package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AuditLogService;
import com.edussafy.clone.domain.admin.domain.enums.AuditAction;
import com.edussafy.clone.domain.admin.dto.response.AuditLogResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/audit-logs")
public class AdminAuditLogController {
    private final AuditLogService service;
    @GetMapping
    public ApiResponse<PageResponse<AuditLogResponse>> getAuditLogs(@com.edussafy.clone.global.security.CurrentUser Long adminId, @RequestParam(required = false) Long actorId, @RequestParam(required = false) AuditAction action,
            @RequestParam(required = false) String targetType, @RequestParam(required = false) Long targetId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getAuditLogs(adminId, actorId, action, targetType, targetId, startDate, endDate, page, size));
    }
    @GetMapping("/{auditLogId}")
    public ApiResponse<AuditLogResponse> getAuditLog(@com.edussafy.clone.global.security.CurrentUser Long adminId, @PathVariable Long auditLogId) { return ApiResponse.ok(service.getAuditLog(adminId, auditLogId)); }
}
