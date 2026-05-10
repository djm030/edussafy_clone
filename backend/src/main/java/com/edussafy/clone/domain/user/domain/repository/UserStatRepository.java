package com.edussafy.clone.domain.user.domain.repository;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatRepository extends JpaRepository<UserStat, Long> {
    Optional<UserStat> findByUser(User user);
}
