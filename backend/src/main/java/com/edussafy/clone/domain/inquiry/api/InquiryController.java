package com.edussafy.clone.domain.inquiry.api;

import com.edussafy.clone.domain.inquiry.application.InquiryService;
import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import com.edussafy.clone.domain.inquiry.dto.request.InquiryCreateRequest;
import com.edussafy.clone.domain.inquiry.dto.request.InquiryUpdateRequest;
import com.edussafy.clone.domain.inquiry.dto.response.InquiryResponse;
import com.edussafy.clone.global.response.ApiResponse;
import com.edussafy.clone.global.response.PageResponse;
import com.edussafy.clone.global.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class InquiryController {
    private final InquiryService inquiryService;

    @GetMapping("/my")
    public ApiResponse<PageResponse<InquiryResponse>> getMyInquiries(@CurrentUser Long currentUserId,
            @RequestParam(required = false) InquiryStatus status, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.ok(inquiryService.getMyInquiries(currentUserId, status, page, size));
    }
    @GetMapping("/{inquiryId}")
    public ApiResponse<InquiryResponse> getInquiry(@PathVariable Long inquiryId, @CurrentUser Long currentUserId) {
        return ApiResponse.ok(inquiryService.getInquiry(inquiryId, currentUserId));
    }
    @PostMapping
    public ApiResponse<InquiryResponse> create(@CurrentUser Long currentUserId, @Valid @RequestBody InquiryCreateRequest request) {
        return ApiResponse.ok(inquiryService.create(currentUserId, request));
    }
    @PatchMapping("/{inquiryId}")
    public ApiResponse<InquiryResponse> update(@PathVariable Long inquiryId, @CurrentUser Long currentUserId,
            @Valid @RequestBody InquiryUpdateRequest request) {
        return ApiResponse.ok(inquiryService.update(inquiryId, currentUserId, request));
    }
    @DeleteMapping("/{inquiryId}")
    public ApiResponse<Void> delete(@PathVariable Long inquiryId, @CurrentUser Long currentUserId) {
        inquiryService.delete(inquiryId, currentUserId);
        return ApiResponse.ok(null);
    }
}
