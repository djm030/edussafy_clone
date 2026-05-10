package com.edussafy.clone.domain.point.dto.response;

public record PointSummaryResponse(Integer scholarshipPoint, Integer totalExp, String levelName, Integer levelNo,
                                   Double attendanceRate, Integer completedLearningCount) { }
