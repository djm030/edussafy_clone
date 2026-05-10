package com.edussafy.clone.domain.inquiry.dto.response;

import com.edussafy.clone.domain.inquiry.domain.enums.InquiryStatus;
import java.time.LocalDateTime;

public record InquiryResponse(Long id, String category, String title, String content, InquiryStatus status,
                              String answerContent, Long answeredById, String answeredByName, LocalDateTime answeredAt,
                              LocalDateTime createdAt, LocalDateTime updatedAt) { }
