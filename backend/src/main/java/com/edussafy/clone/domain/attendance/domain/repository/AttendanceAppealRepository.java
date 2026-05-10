package com.edussafy.clone.domain.attendance.domain.repository;

import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.user.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceAppealRepository extends JpaRepository<AttendanceAppeal, Long> {
    Page<AttendanceAppeal> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);
}
