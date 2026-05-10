package com.edussafy.clone.domain.agreement.dto.mapper;

import com.edussafy.clone.domain.agreement.domain.entity.Agreement;
import com.edussafy.clone.domain.agreement.domain.entity.UserAgreement;
import com.edussafy.clone.domain.agreement.dto.response.AgreementResponse;
import com.edussafy.clone.domain.agreement.dto.response.UserAgreementResponse;
import org.springframework.stereotype.Component;

@Component
public class AgreementDtoMapper {
    public AgreementResponse toResponse(Agreement agreement) {
        return new AgreementResponse(agreement.getId(), agreement.getCategory(), agreement.getTargetType(), agreement.getTargetId(),
                agreement.getTitle(), agreement.getContentHtml(), agreement.getAttachmentFile() == null ? null : agreement.getAttachmentFile().getId(),
                agreement.getAgreementType(), agreement.getVersion(), agreement.getIsRequired(), agreement.getIsActive(), agreement.getSortOrder());
    }
    public UserAgreementResponse toUserResponse(UserAgreement userAgreement) {
        return new UserAgreementResponse(userAgreement.getId(), userAgreement.getAgreement().getId(), userAgreement.getAgreement().getTitle(),
                userAgreement.getAgreedVersion(), userAgreement.getAgreedAt());
    }
}
