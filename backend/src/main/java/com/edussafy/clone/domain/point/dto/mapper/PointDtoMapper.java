package com.edussafy.clone.domain.point.dto.mapper;

import com.edussafy.clone.domain.point.domain.entity.PointTransaction;
import com.edussafy.clone.domain.point.dto.response.PointSummaryResponse;
import com.edussafy.clone.domain.point.dto.response.PointTransactionResponse;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import org.springframework.stereotype.Component;

@Component
public class PointDtoMapper {
    public PointSummaryResponse toSummaryResponse(UserStat stat) {
        if (stat == null) {
            return new PointSummaryResponse(0, 0, "Lv.1", 1, 0.0, 0);
        }
        return new PointSummaryResponse(nullToZero(stat.getScholarshipPoint()), nullToZero(stat.getTotalExp()),
                stat.getLevelName() == null ? "Lv." + nullToOne(stat.getLevelNo()) : stat.getLevelName(),
                nullToOne(stat.getLevelNo()), stat.getAttendanceRate() == null ? 0.0 : stat.getAttendanceRate(),
                nullToZero(stat.getCompletedLearningCount()));
    }
    public PointTransactionResponse toTransactionResponse(PointTransaction tx) {
        return new PointTransactionResponse(tx.getId(), tx.getTransactionType(), tx.getPointAmount(), tx.getExpAmount(),
                tx.getReason(), tx.getTargetType(), tx.getTargetId(), tx.getCreatedAt());
    }
    private int nullToZero(Integer value) { return value == null ? 0 : value; }
    private int nullToOne(Integer value) { return value == null ? 1 : value; }
}
