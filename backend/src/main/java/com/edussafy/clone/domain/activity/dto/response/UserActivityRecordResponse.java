package com.edussafy.clone.domain.activity.dto.response;

import com.edussafy.clone.domain.activity.domain.enums.ActivityType;
import java.time.LocalDate;

public record UserActivityRecordResponse(Long id, ActivityType activityType, String title, String description,
                                         String organization, LocalDate activityDate, String resultText,
                                         Long evidenceFileId) { }
