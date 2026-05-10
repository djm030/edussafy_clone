package com.edussafy.clone.domain.course.domain.repository;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseWeek;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseWeekRepository extends JpaRepository<CourseWeek, Long> {
    List<CourseWeek> findByCourseOrderBySortOrderAscWeekNoAsc(Course course);
}
