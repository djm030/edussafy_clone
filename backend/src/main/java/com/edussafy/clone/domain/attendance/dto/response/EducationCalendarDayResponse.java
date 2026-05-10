package com.edussafy.clone.domain.attendance.dto.response;

import com.edussafy.clone.domain.attendance.domain.enums.EducationDayType;
import java.time.LocalDate;

public record EducationCalendarDayResponse(Long id, Long courseId, LocalDate calendarDate, EducationDayType dayType,
                                           Boolean isEducationDay, String title, String description) { }
