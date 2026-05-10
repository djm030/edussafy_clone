package com.edussafy.clone.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;

public record InquiryUpdateRequest(String category, @NotBlank String title, @NotBlank String content) { }
