package com.edussafy.clone.domain.course.dto.mapper;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.course.domain.entity.CourseSession;
import com.edussafy.clone.domain.course.domain.entity.CourseWeek;
import com.edussafy.clone.domain.course.dto.response.CourseResponse;
import com.edussafy.clone.domain.course.dto.response.CourseSessionResponse;
import com.edussafy.clone.domain.course.dto.response.CourseWeekResponse;
import org.springframework.stereotype.Component;

@Component
public class CourseDtoMapper {
    public CourseResponse toCourseResponse(Course course) {
        return new CourseResponse(course.getId(), course.getTitle(), course.getDescription(), course.getGeneration(),
                course.getRegion(), course.getClassNo(), course.getInstructorName(), course.getStatus(),
                course.getStartDate(), course.getEndDate());
    }

    public CourseWeekResponse toWeekResponse(CourseWeek week) {
        return new CourseWeekResponse(week.getId(), week.getWeekNo(), week.getTitle(), week.getStartDate(),
                week.getEndDate(), week.getSortOrder());
    }

    public CourseSessionResponse toSessionResponse(CourseSession session) {
        return new CourseSessionResponse(session.getId(), session.getCourse().getId(),
                session.getWeek() == null ? null : session.getWeek().getId(), session.getTitle(), session.getSubtitle(),
                session.getSessionType(), session.getSessionDate(), session.getStartAt(), session.getEndAt(),
                session.getInstructorName(), session.getLocation(), session.getLiveUrl(), session.getReplayUrl(),
                session.getMaterialPost() == null ? null : session.getMaterialPost().getId(),
                session.getIsRequired(), session.getSortOrder());
    }
}
