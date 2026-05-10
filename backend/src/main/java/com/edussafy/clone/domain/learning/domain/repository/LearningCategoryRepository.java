package com.edussafy.clone.domain.learning.domain.repository;

import com.edussafy.clone.domain.learning.domain.entity.LearningCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningCategoryRepository extends JpaRepository<LearningCategory, Long> {
    List<LearningCategory> findAllByOrderByIdAsc();
}
