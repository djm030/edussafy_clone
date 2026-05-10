package com.edussafy.clone.domain.inquiry.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record InquiryCreateRequest(String category, @NotBlank String title, @NotBlank String content, List<Long> fileIds) { }
