package com.edussafy.clone.domain.agreement.api;

import com.edussafy.clone.domain.agreement.application.AgreementService;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementTargetType;
import com.edussafy.clone.domain.agreement.dto.response.AgreementResponse;
import com.edussafy.clone.domain.agreement.dto.response.UserAgreementResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agreements")
public class AgreementController {
    private final AgreementService agreementService;

    @GetMapping
    public ApiResponse<List<AgreementResponse>> getAgreements(@RequestParam(required = false) AgreementCategory category,
            @RequestParam(required = false) AgreementTargetType targetType, @RequestParam(required = false) Long targetId,
            @RequestParam(required = false) Boolean requiredOnly) {
        return ApiResponse.ok(agreementService.getAgreements(category, targetType, targetId, requiredOnly));
    }
    @GetMapping("/{agreementId}")
    public ApiResponse<AgreementResponse> getAgreement(@PathVariable Long agreementId) {
        return ApiResponse.ok(agreementService.getAgreement(agreementId));
    }
    @PostMapping("/{agreementId}/agree")
    public ApiResponse<UserAgreementResponse> agree(@PathVariable Long agreementId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(agreementService.agree(agreementId, currentUserId));
    }
    @GetMapping("/my")
    public ApiResponse<PageResponse<UserAgreementResponse>> getMyAgreements(@CurrentUser Long currentUserId,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(agreementService.getMyAgreements(currentUserId, page, size));
    }
}
