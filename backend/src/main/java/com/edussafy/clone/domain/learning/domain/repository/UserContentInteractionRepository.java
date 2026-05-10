package com.edussafy.clone.domain.learning.domain.repository;

import com.edussafy.clone.domain.learning.domain.entity.UserContentInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserContentInteractionRepository extends JpaRepository<UserContentInteraction, Long> {
}
