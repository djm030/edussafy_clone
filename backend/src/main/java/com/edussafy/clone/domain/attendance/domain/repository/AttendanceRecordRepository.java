package com.edussafy.clone.domain.attendance.domain.repository;

import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.user.domain.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long>, JpaSpecificationExecutor<AttendanceRecord> {
    Optional<AttendanceRecord> findByUserAndAttendanceDate(User user, LocalDate attendanceDate);
    List<AttendanceRecord> findByUserAndAttendanceDateBetweenOrderByAttendanceDateAsc(User user, LocalDate startDate, LocalDate endDate);
    List<AttendanceRecord> findByUserAndCourseIdAndAttendanceDateBetweenOrderByAttendanceDateAsc(User user, Long courseId, LocalDate startDate, LocalDate endDate);
    Page<AttendanceRecord> findByUserAndAttendanceDateBetweenOrderByAttendanceDateDesc(User user, LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<AttendanceRecord> findByUserAndStatusAndAttendanceDateBetweenOrderByAttendanceDateDesc(User user, AttendanceStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable);
}
