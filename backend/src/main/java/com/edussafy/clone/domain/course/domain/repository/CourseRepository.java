package com.edussafy.clone.domain.course.domain.repository;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.enums.CourseStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatusOrderByStartDateDescIdDesc(CourseStatus status);
}
