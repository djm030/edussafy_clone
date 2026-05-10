package com.edussafy.clone.domain.admin.dto.request;

import com.edussafy.clone.domain.attendance.domain.enums.EducationDayType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminCalendarDayRequest(Long courseId, @NotNull LocalDate calendarDate, @NotNull EducationDayType dayType, Boolean isEducationDay, String title, String description) {
}
