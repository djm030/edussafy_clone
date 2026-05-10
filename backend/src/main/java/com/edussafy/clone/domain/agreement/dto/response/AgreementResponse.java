package com.edussafy.clone.domain.agreement.dto.response;

import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementTargetType;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementType;

public record AgreementResponse(Long id, AgreementCategory category, AgreementTargetType targetType, Long targetId,
                                String title, String contentHtml, Long attachmentFileId, AgreementType agreementType,
                                String version, Boolean isRequired, Boolean isActive, Integer sortOrder) { }
