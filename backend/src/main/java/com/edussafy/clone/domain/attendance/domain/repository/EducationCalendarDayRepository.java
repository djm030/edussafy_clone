package com.edussafy.clone.domain.attendance.domain.repository;

import com.edussafy.clone.domain.attendance.domain.entity.EducationCalendarDay;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationCalendarDayRepository extends JpaRepository<EducationCalendarDay, Long> {
    List<EducationCalendarDay> findByCourseIdAndCalendarDateBetweenOrderByCalendarDateAsc(Long courseId, LocalDate startDate, LocalDate endDate);
    List<EducationCalendarDay> findByCalendarDateBetweenOrderByCalendarDateAsc(LocalDate startDate, LocalDate endDate);
}
