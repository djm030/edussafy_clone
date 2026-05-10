package com.edussafy.clone.domain.agreement.dto.response;

import java.time.LocalDateTime;

public record UserAgreementResponse(Long id, Long agreementId, String agreementTitle, String agreedVersion,
                                    LocalDateTime agreedAt) { }
