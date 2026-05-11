package com.edussafy.clone.domain.attendance.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.attendance.domain.entity.AttendanceAppeal;
import com.edussafy.clone.domain.attendance.domain.entity.AttendanceRecord;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceAppealStatus;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceIssueType;
import com.edussafy.clone.domain.attendance.domain.enums.AttendanceStatus;
import com.edussafy.clone.domain.bookmark.domain.entity.UserBookmark;
import com.edussafy.clone.domain.bookmark.domain.enums.BookmarkTargetType;
import com.edussafy.clone.domain.point.domain.entity.PointTransaction;
import com.edussafy.clone.domain.point.domain.enums.PointTransactionType;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class CampusActivityDomainBehaviorTest {

    @Test
    void attendanceAppeal_defaults_to_submitted_and_records_review_decision() {
        User student = user(1L, UserRole.STUDENT);
        User operator = user(2L, UserRole.OPERATOR);
        AttendanceRecord record = AttendanceRecord.builder()
                .id(1L)
                .user(student)
                .attendanceDate(LocalDate.of(2026, 5, 11))
                .status(AttendanceStatus.LATE)
                .build();
        AttendanceAppeal appeal = AttendanceAppeal.builder()
                .attendanceRecord(record)
                .user(student)
                .appealType(AttendanceIssueType.LATE)
                .reason("network issue")
                .build();

        assertThat(appeal.getAppealStatus()).isEqualTo(AttendanceAppealStatus.SUBMITTED);

        appeal.review(AttendanceAppealStatus.APPROVED, operator, "accepted");

        assertThat(appeal.getAppealStatus()).isEqualTo(AttendanceAppealStatus.APPROVED);
        assertThat(appeal.getReviewedBy()).isSameAs(operator);
        assertThat(appeal.getReviewedAt()).isNotNull();
        assertThat(appeal.getReviewComment()).isEqualTo("accepted");
    }

    @Test
    void pointTransaction_and_bookmark_apply_safe_defaults() {
        User student = user(1L, UserRole.STUDENT);

        PointTransaction point = PointTransaction.builder()
                .user(student)
                .transactionType(PointTransactionType.EARN)
                .reason("quest")
                .build();
        UserBookmark bookmark = UserBookmark.builder()
                .user(student)
                .targetType(BookmarkTargetType.LEARNING_CONTENT)
                .targetId(100L)
                .build();

        assertThat(point.getPointAmount()).isZero();
        assertThat(point.getExpAmount()).isZero();
        assertThat(point.getCreatedAt()).isNotNull();
        assertThat(bookmark.getTargetType()).isEqualTo(BookmarkTargetType.LEARNING_CONTENT);
        assertThat(bookmark.getCreatedAt()).isNotNull();
    }

    private User user(Long id, UserRole role) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .password("encoded")
                .name("User " + id)
                .role(role)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
