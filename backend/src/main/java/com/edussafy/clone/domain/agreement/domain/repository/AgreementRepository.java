package com.edussafy.clone.domain.agreement.domain.repository;

import com.edussafy.clone.domain.agreement.domain.entity.Agreement;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementCategory;
import com.edussafy.clone.domain.agreement.domain.enums.AgreementTargetType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AgreementRepository extends JpaRepository<Agreement, Long>, JpaSpecificationExecutor<Agreement> {
    Optional<Agreement> findByIdAndIsActiveTrue(Long id);
}
