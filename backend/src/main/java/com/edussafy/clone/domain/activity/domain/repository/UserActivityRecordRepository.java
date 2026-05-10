package com.edussafy.clone.domain.activity.domain.repository;

import com.edussafy.clone.domain.activity.domain.entity.UserActivityRecord;
import com.edussafy.clone.domain.activity.domain.enums.ActivityType;
import com.edussafy.clone.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRecordRepository extends JpaRepository<UserActivityRecord, Long> {
    Page<UserActivityRecord> findByUserOrderByActivityDateDescIdDesc(User user, Pageable pageable);
    Page<UserActivityRecord> findByUserAndActivityTypeOrderByActivityDateDescIdDesc(User user, ActivityType activityType, Pageable pageable);
}
