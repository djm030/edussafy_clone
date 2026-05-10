package com.edussafy.clone.domain.point.domain.repository;

import com.edussafy.clone.domain.point.domain.entity.PointTransaction;
import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long>, JpaSpecificationExecutor<PointTransaction> {
    Page<PointTransaction> findByUserAndCreatedAtBetweenOrderByCreatedAtDesc(User user, LocalDateTime startAt, LocalDateTime endAt, Pageable pageable);
    Page<PointTransaction> findByUserAndTransactionTypeAndCreatedAtBetweenOrderByCreatedAtDesc(User user, PointTransactionType type, LocalDateTime startAt, LocalDateTime endAt, Pageable pageable);
}
