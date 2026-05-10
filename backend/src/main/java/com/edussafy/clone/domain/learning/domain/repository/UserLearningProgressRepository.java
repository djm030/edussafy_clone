package com.edussafy.clone.domain.learning.domain.repository;

import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.domain.entity.UserLearningProgress;
import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLearningProgressRepository extends JpaRepository<UserLearningProgress, Long> {
    Optional<UserLearningProgress> findByUserAndContent(User user, LearningContent content);
    Page<UserLearningProgress> findByUser(User user, Pageable pageable);
    Page<UserLearningProgress> findByUserAndProgressStatus(User user, LearningProgressStatus progressStatus, Pageable pageable);
}
