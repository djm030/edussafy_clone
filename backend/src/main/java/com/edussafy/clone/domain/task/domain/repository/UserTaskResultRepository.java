package com.edussafy.clone.domain.task.domain.repository;

import com.edussafy.clone.domain.task.domain.entity.CourseTask;
import com.edussafy.clone.domain.task.domain.entity.UserTaskResult;
import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskResultRepository extends JpaRepository<UserTaskResult, Long> {
    Optional<UserTaskResult> findByTaskAndUser(CourseTask task, User user);
    Page<UserTaskResult> findByUserAndResultStatusOrderByUpdatedAtDesc(User user, TaskResultStatus status, Pageable pageable);
}
