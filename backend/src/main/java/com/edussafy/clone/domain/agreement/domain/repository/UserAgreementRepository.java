package com.edussafy.clone.domain.agreement.domain.repository;

import com.edussafy.clone.domain.agreement.domain.entity.Agreement;
import com.edussafy.clone.domain.agreement.domain.entity.UserAgreement;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAgreementRepository extends JpaRepository<UserAgreement, Long>, JpaSpecificationExecutor<UserAgreement> {
    Optional<UserAgreement> findByAgreementAndUserAndAgreedVersion(Agreement agreement, User user, String agreedVersion);
    Page<UserAgreement> findByUserOrderByAgreedAtDesc(User user, Pageable pageable);
}
