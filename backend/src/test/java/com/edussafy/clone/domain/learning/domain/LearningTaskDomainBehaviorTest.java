package com.edussafy.clone.domain.learning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.course.domain.entity.Course;
import com.edussafy.clone.domain.learning.domain.entity.LearningContent;
import com.edussafy.clone.domain.learning.domain.entity.UserLearningProgress;
import com.edussafy.clone.domain.learning.domain.enums.LearningContentType;
import com.edussafy.clone.domain.learning.domain.enums.LearningProgressStatus;
import com.edussafy.clone.domain.task.domain.entity.CourseTask;
import com.edussafy.clone.domain.task.domain.entity.UserTaskResult;
import com.edussafy.clone.domain.task.domain.enums.CourseTaskType;
import com.edussafy.clone.domain.task.domain.enums.TaskResultStatus;
import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class LearningTaskDomainBehaviorTest {

    @Test
    void learningProgress_starts_on_first_save_and_completion_sets_full_progress() {
        UserLearningProgress progress = UserLearningProgress.builder()
                .user(user(1L))
                .content(content(10L))
                .build();

        progress.saveProgress(45.5, 120, LearningProgressStatus.IN_PROGRESS);

        assertThat(progress.getProgressStatus()).isEqualTo(LearningProgressStatus.IN_PROGRESS);
        assertThat(progress.getProgressRate()).isEqualTo(45.5);
        assertThat(progress.getLastPositionSeconds()).isEqualTo(120);
        assertThat(progress.getStartedAt()).isNotNull();
        assertThat(progress.getLastAccessedAt()).isNotNull();

        progress.complete();

        assertThat(progress.getProgressStatus()).isEqualTo(LearningProgressStatus.COMPLETED);
        assertThat(progress.getProgressRate()).isEqualTo(100.0);
        assertThat(progress.getCompletedAt()).isNotNull();
    }

    @Test
    void taskResult_submit_increments_attempt_and_result_update_sets_completion_time() {
        UserTaskResult result = UserTaskResult.builder()
                .task(task(20L))
                .user(user(1L))
                .build();

        result.submit("{\"answer\":true}");

        assertThat(result.getResultStatus()).isEqualTo(TaskResultStatus.SUBMITTED);
        assertThat(result.getAttemptCount()).isEqualTo(1);
        assertThat(result.getSubmittedAt()).isNotNull();

        result.updateResult(TaskResultStatus.PASSED, BigDecimal.valueOf(90), BigDecimal.valueOf(80), null, result.getAnswerData());

        assertThat(result.getResultStatus()).isEqualTo(TaskResultStatus.PASSED);
        assertThat(result.getScore()).isEqualByComparingTo("90");
        assertThat(result.getCompletedAt()).isNotNull();
    }

    private LearningContent content(Long id) {
        return LearningContent.builder()
                .id(id)
                .title("lecture")
                .contentType(LearningContentType.VIDEO)
                .build();
    }

    private CourseTask task(Long id) {
        return CourseTask.builder()
                .id(id)
                .course(Course.builder().id(1L).title("course").build())
                .title("quest")
                .taskType(CourseTaskType.QUEST)
                .build();
    }

    private User user(Long id) {
        return User.builder()
                .id(id)
                .email("user" + id + "@example.com")
                .password("encoded")
                .name("User " + id)
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
