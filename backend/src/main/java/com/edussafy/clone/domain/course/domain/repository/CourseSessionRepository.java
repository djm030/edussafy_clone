package com.edussafy.clone.domain.course.domain.repository;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseSession;
import com.edussafy.clone.domain.course.domain.entity.CourseWeek;
import com.edussafy.clone.domain.course.domain.enums.CourseSessionType;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseSessionRepository extends JpaRepository<CourseSession, Long> {
    Page<CourseSession> findByCourseAndWeekOrderBySortOrderAscIdAsc(Course course, CourseWeek week, Pageable pageable);

    @Query("""
            select s from CourseSession s
            where (:courseId is null or s.course.id = :courseId)
              and (:weekId is null or s.week.id = :weekId)
              and s.sessionType = :sessionType
              and (:keyword is null or lower(s.title) like lower(concat('%', :keyword, '%')))
            order by s.sessionDate desc, s.id desc
            """)
    Page<CourseSession> searchSessions(@Param("courseId") Long courseId,
                                       @Param("weekId") Long weekId,
                                       @Param("sessionType") CourseSessionType sessionType,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);

    @Query("""
            select s from CourseSession s
            where s.course.id = :courseId
              and (:startDate is null or s.sessionDate >= :startDate)
              and (:endDate is null or s.sessionDate <= :endDate)
            order by s.sessionDate asc, s.sortOrder asc, s.id asc
            """)
    Page<CourseSession> findCourseSessionsInRange(@Param("courseId") Long courseId,
                                                  @Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  Pageable pageable);

    @Query("""
            select s from CourseSession s
            where (:generation is null or s.course.generation = :generation)
              and (:region is null or s.course.region = :region)
              and (:classNo is null or s.course.classNo = :classNo)
              and s.sessionType = :sessionType
              and (:keyword is null or lower(s.title) like lower(concat('%', :keyword, '%')))
            order by s.sessionDate desc, s.id desc
            """)
    Page<CourseSession> findMyReplays(@Param("generation") Integer generation,
                                      @Param("region") String region,
                                      @Param("classNo") Integer classNo,
                                      @Param("sessionType") CourseSessionType sessionType,
                                      @Param("keyword") String keyword,
                                      Pageable pageable);
}
