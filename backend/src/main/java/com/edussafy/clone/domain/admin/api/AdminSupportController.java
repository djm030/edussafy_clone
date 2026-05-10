package com.edussafy.clone.domain.admin.api;

import com.edussafy.clone.domain.admin.application.AdminSupportService;
import com.edussafy.clone.domain.agreement.dto.response.AgreementResponse;
import com.edussafy.clone.domain.agreement.dto.response.UserAgreementResponse;
import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import com.edussafy.clone.domain.inquiry.dto.response.InquiryResponse;
import com.edussafy.clone.domain.notification.domain.enums.NotificationType;
import com.edussafy.clone.domain.notification.dto.response.NotificationResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminSupportController {
    private final AdminSupportService service;

    @GetMapping("/inquiries")
    public ApiResponse<PageResponse<InquiryResponse>> inquiries(@CurrentUser Long adminId, @RequestParam(required = false) InquiryStatus status,
            @RequestParam(required = false) Long userId, @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getInquiries(adminId, status, userId, category, page, size));
    }

    @GetMapping("/inquiries/{inquiryId}")
    public ApiResponse<InquiryResponse> inquiry(@CurrentUser Long adminId, @PathVariable Long inquiryId) {
        return ApiResponse.ok(service.getInquiry(adminId, inquiryId));
    }

    @PostMapping("/inquiries/{inquiryId}/answer")
    public ApiResponse<InquiryResponse> answerInquiry(@CurrentUser Long adminId, @PathVariable Long inquiryId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.answerInquiry(adminId, inquiryId, request));
    }

    @PostMapping("/inquiries/{inquiryId}/close")
    public ApiResponse<InquiryResponse> closeInquiry(@CurrentUser Long adminId, @PathVariable Long inquiryId) {
        return ApiResponse.ok(service.closeInquiry(adminId, inquiryId));
    }

    @PostMapping("/notifications")
    public ApiResponse<NotificationResponse> sendNotification(@CurrentUser Long adminId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.sendNotification(adminId, request));
    }

    @GetMapping("/notifications")
    public ApiResponse<PageResponse<NotificationResponse>> notifications(@CurrentUser Long adminId, @RequestParam(required = false) Long receiverId,
            @RequestParam(required = false) NotificationType type, @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getNotifications(adminId, receiverId, type, isRead, page, size));
    }

    @PostMapping("/agreements")
    public ApiResponse<AgreementResponse> createAgreement(@CurrentUser Long adminId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.createAgreement(adminId, request));
    }

    @PatchMapping("/agreements/{agreementId}")
    public ApiResponse<AgreementResponse> updateAgreement(@CurrentUser Long adminId, @PathVariable Long agreementId, @RequestBody Map<String, Object> request) {
        return ApiResponse.ok(service.updateAgreement(adminId, agreementId, request));
    }

    @RequestMapping(value = "/agreements/{agreementId}/inactive", method = {RequestMethod.PATCH, RequestMethod.POST})
    public ApiResponse<Void> inactiveAgreement(@CurrentUser Long adminId, @PathVariable Long agreementId) {
        service.inactiveAgreement(adminId, agreementId);
        return ApiResponse.ok();
    }

    @GetMapping("/user-agreements")
    public ApiResponse<PageResponse<UserAgreementResponse>> userAgreements(@CurrentUser Long adminId, @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long agreementId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(service.getUserAgreements(adminId, userId, agreementId, page, size));
    }
}
