package com.edussafy.clone.domain.user.domain.repository;

import com.edussafy.clone.domain.user.domain.entity.PasswordChangeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordChangeHistoryRepository extends JpaRepository<PasswordChangeHistory, Long> {
}
