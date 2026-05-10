package com.edussafy.clone.domain.agreement.application;

import com.edussafy.clone.domain.agreement.domain.entity.Agreement;
import com.edussafy.clone.domain.agreement.domain.entity.UserAgreement;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementTargetType;
import com.edussafy.clone.domain.agreement.domain.repository.AgreementRepository;
import com.edussafy.clone.domain.agreement.domain.repository.UserAgreementRepository;
import com.edussafy.clone.domain.agreement.dto.mapper.AgreementDtoMapper;
import com.edussafy.clone.domain.agreement.dto.response.AgreementResponse;
import com.edussafy.clone.domain.agreement.dto.response.UserAgreementResponse;
import com.edussafy.clone.domain.agreement.exception.AgreementNotFoundException;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import com.edussafy.clone.global.response.PageResponse;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgreementService {
    private final AgreementRepository agreementRepository;
    private final UserAgreementRepository userAgreementRepository;
    private final UserRepository userRepository;
    private final AgreementDtoMapper mapper;

    public List<AgreementResponse> getAgreements(AgreementCategory category, AgreementTargetType targetType, Long targetId, Boolean requiredOnly) {
        return agreementRepository.findAll(spec(category, targetType, targetId, requiredOnly)).stream().map(mapper::toResponse).toList();
    }
    public AgreementResponse getAgreement(Long agreementId) { return mapper.toResponse(getActiveAgreement(agreementId)); }
    @Transactional
    public UserAgreementResponse agree(Long agreementId, Long userId) {
        Agreement agreement = getActiveAgreement(agreementId);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        UserAgreement userAgreement = userAgreementRepository.findByAgreementAndUserAndAgreedVersion(agreement, user, agreement.getVersion())
                .orElseGet(() -> userAgreementRepository.save(UserAgreement.builder().agreement(agreement).user(user).agreedVersion(agreement.getVersion()).build()));
        return mapper.toUserResponse(userAgreement);
    }
    public PageResponse<UserAgreementResponse> getMyAgreements(Long userId, int page, int size) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return PageResponse.from(userAgreementRepository.findByUserOrderByAgreedAtDesc(user, PageRequest.of(page, size)).map(mapper::toUserResponse));
    }
    private Agreement getActiveAgreement(Long id) { return agreementRepository.findByIdAndIsActiveTrue(id).orElseThrow(AgreementNotFoundException::new); }
    private Specification<Agreement> spec(AgreementCategory category, AgreementTargetType targetType, Long targetId, Boolean requiredOnly) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(root.get("isActive")));
            if (category != null) predicates.add(cb.equal(root.get("category"), category));
            if (targetType != null) predicates.add(cb.equal(root.get("targetType"), targetType));
            if (targetId != null) predicates.add(cb.equal(root.get("targetId"), targetId));
            if (Boolean.TRUE.equals(requiredOnly)) predicates.add(cb.isTrue(root.get("isRequired")));
            query.orderBy(cb.asc(root.get("sortOrder")), cb.asc(root.get("id")));
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }
}
