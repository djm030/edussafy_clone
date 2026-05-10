package com.edussafy.clone.domain.activity.dto.mapper;

import com.edussafy.clone.domain.activity.domain.entity.UserActivityRecord;
import com.edussafy.clone.domain.activity.dto.response.UserActivityRecordResponse;
import org.springframework.stereotype.Component;

@Component
public class ActivityDtoMapper {
    public UserActivityRecordResponse toResponse(UserActivityRecord record) {
        return new UserActivityRecordResponse(record.getId(), record.getActivityType(), record.getTitle(), record.getDescription(),
                record.getOrganization(), record.getActivityDate(), record.getResultText(), record.getEvidenceFile() == null ? null : record.getEvidenceFile().getId());
    }
}
