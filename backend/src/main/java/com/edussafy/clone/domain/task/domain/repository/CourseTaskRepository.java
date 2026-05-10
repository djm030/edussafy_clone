package com.edussafy.clone.domain.task.domain.repository;

import com.edussafy.clone.domain.task.domain.entity.CourseTask;
import com.edussafy.clone.domain.task.domain.enums.CourseTaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseTaskRepository extends JpaRepository<CourseTask, Long> {
    @Query("""
            select t from CourseTask t
            where (:courseId is null or t.course.id = :courseId)
              and (:taskType is null or t.taskType = :taskType)
            order by t.openAt desc, t.id desc
            """)
    Page<CourseTask> search(@Param("courseId") Long courseId, @Param("taskType") CourseTaskType taskType, Pageable pageable);
}
