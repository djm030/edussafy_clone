package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminPointService;
import com.edussafy.clone.domain.admin.dto.request.AdminPointAdjustRequest;
import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.point.dto.response.PointTransactionResponse;
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
@RequestMapping("/api/v1/admin/points")
public class AdminPointController {
    private final AdminPointService service;
    @GetMapping("/users/{userId}")
    public ApiResponse<PointSummaryResponse> getUserPoint(@CurrentUser Long adminId, @PathVariable Long userId) { return ApiResponse.ok(service.getUserPoint(adminId, userId)); }
    @PostMapping("/transactions")
    public ApiResponse<PointTransactionResponse> adjust(@CurrentUser Long adminId, @Valid @RequestBody AdminPointAdjustRequest request) { return ApiResponse.ok(service.adjust(adminId, request)); }
    @GetMapping("/transactions")
    public ApiResponse<PageResponse<PointTransactionResponse>> getTransactions(@CurrentUser Long adminId, @RequestParam(required = false) Long userId, @RequestParam(required = false) PointTransactionType transactionType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getTransactions(adminId, userId, transactionType, startDate, endDate, page, size));
    }
}
