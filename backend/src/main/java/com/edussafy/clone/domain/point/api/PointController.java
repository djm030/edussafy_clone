package com.edussafy.clone.domain.point.api;

import com.edussafy.clone.domain.point.application.PointService;
import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.point.dto.response.PointTransactionResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/points")
public class PointController {
    private final PointService pointService;

    @GetMapping("/my/summary")
    public ApiResponse<PointSummaryResponse> getMySummary(@CurrentUser Long currentUserId) {
        return ApiResponse.ok(pointService.getMySummary(currentUserId));
    }

    @GetMapping("/my/transactions")
    public ApiResponse<PageResponse<PointTransactionResponse>> getMyTransactions(
            @CurrentUser Long currentUserId,
            @RequestParam(required = false) PointTransactionType transactionType,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(pointService.getMyTransactions(currentUserId, transactionType, startDate, endDate, page, size));
    }
}
