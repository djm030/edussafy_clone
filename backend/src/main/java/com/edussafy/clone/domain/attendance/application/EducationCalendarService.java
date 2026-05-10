package com.edussafy.clone.domain.attendance.application;

import com.edussafy.clone.domain.attendance.domain.repository.EducationCalendarDayRepository;
import com.edussafy.clone.domain.attendance.dto.mapper.AttendanceDtoMapper;
import com.edussafy.clone.domain.attendance.dto.response.EducationCalendarDayResponse;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationCalendarService {
    private final EducationCalendarDayRepository repository;
    private final AttendanceDtoMapper mapper;

    public List<EducationCalendarDayResponse> getCalendar(Long courseId, Integer year, Integer month) {
        YearMonth ym = YearMonth.of(year == null ? LocalDate.now().getYear() : year, month == null ? LocalDate.now().getMonthValue() : month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        return (courseId == null
                ? repository.findByCalendarDateBetweenOrderByCalendarDateAsc(start, end)
                : repository.findByCourseIdAndCalendarDateBetweenOrderByCalendarDateAsc(courseId, start, end))
                .stream().map(mapper::toCalendarResponse).toList();
    }
}
