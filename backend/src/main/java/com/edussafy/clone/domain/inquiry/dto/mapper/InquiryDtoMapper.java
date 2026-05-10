package com.edussafy.clone.domain.inquiry.dto.mapper;

import com.edussafy.clone.domain.inquiry.domain.entity.Inquiry;
import com.edussafy.clone.domain.inquiry.dto.response.InquiryResponse;
import org.springframework.stereotype.Component;

@Component
public class InquiryDtoMapper {
    public InquiryResponse toResponse(Inquiry inquiry) {
        return new InquiryResponse(inquiry.getId(), inquiry.getCategory(), inquiry.getTitle(), inquiry.getContent(), inquiry.getStatus(),
                inquiry.getAnswerContent(), inquiry.getAnsweredBy() == null ? null : inquiry.getAnsweredBy().getId(),
                inquiry.getAnsweredBy() == null ? null : inquiry.getAnsweredBy().getName(), inquiry.getAnsweredAt(),
                inquiry.getCreatedAt(), inquiry.getUpdatedAt());
    }
}
