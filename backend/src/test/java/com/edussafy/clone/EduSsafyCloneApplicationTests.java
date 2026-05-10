package com.edussafy.clone;

import com.edussafy.clone.domain.board.domain.repository.BoardCategoryRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardCommentRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardPostRepository;
import com.edussafy.clone.domain.board.domain.repository.BoardRepository;
import com.edussafy.clone.domain.activity.domain.repository.UserActivityRecordRepository;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceAppealRepository;
import com.edussafy.clone.domain.attendance.domain.repository.AttendanceRecordRepository;
import com.edussafy.clone.domain.attendance.domain.repository.EducationCalendarDayRepository;
import com.edussafy.clone.domain.bookmark.domain.repository.UserBookmarkRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseSessionRepository;
import com.edussafy.clone.domain.course.domain.repository.CourseWeekRepository;
import com.edussafy.clone.domain.learning.domain.repository.LearningCategoryRepository;
import com.edussafy.clone.domain.learning.domain.repository.LearningContentRepository;
import com.edussafy.clone.domain.learning.domain.repository.UserContentInteractionRepository;
import com.edussafy.clone.domain.learning.domain.repository.UserLearningProgressRepository;
import com.edussafy.clone.domain.point.domain.repository.PointTransactionRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyCategoryRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyParticipantRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyQuestionRepository;
import com.edussafy.clone.domain.survey.domain.repository.SurveyRepository;
import com.edussafy.clone.domain.task.domain.repository.CourseTaskRepository;
import com.edussafy.clone.domain.task.domain.repository.UserTaskResultRepository;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.global.file.FileResourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(properties = {
        "spring.autoconfigure.exclude="
                + "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration,"
                + "org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
})
class EduSsafyCloneApplicationTests {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BoardRepository boardRepository;

    @MockBean
    private BoardCategoryRepository boardCategoryRepository;

    @MockBean
    private BoardPostRepository boardPostRepository;

    @MockBean
    private BoardCommentRepository boardCommentRepository;

    @MockBean
    private FileResourceRepository fileResourceRepository;

    @MockBean
    private CourseRepository courseRepository;

    @MockBean
    private CourseWeekRepository courseWeekRepository;

    @MockBean
    private CourseSessionRepository courseSessionRepository;

    @MockBean
    private LearningCategoryRepository learningCategoryRepository;

    @MockBean
    private LearningContentRepository learningContentRepository;

    @MockBean
    private UserContentInteractionRepository userContentInteractionRepository;

    @MockBean
    private UserLearningProgressRepository userLearningProgressRepository;

    @MockBean
    private UserStatRepository userStatRepository;

    @MockBean
    private AttendanceRecordRepository attendanceRecordRepository;

    @MockBean
    private AttendanceAppealRepository attendanceAppealRepository;

    @MockBean
    private EducationCalendarDayRepository educationCalendarDayRepository;

    @MockBean
    private PointTransactionRepository pointTransactionRepository;

    @MockBean
    private UserBookmarkRepository userBookmarkRepository;

    @MockBean
    private SurveyCategoryRepository surveyCategoryRepository;

    @MockBean
    private SurveyRepository surveyRepository;

    @MockBean
    private SurveyQuestionRepository surveyQuestionRepository;

    @MockBean
    private SurveyParticipantRepository surveyParticipantRepository;

    @MockBean
    private CourseTaskRepository courseTaskRepository;

    @MockBean
    private UserTaskResultRepository userTaskResultRepository;

    @MockBean
    private UserActivityRecordRepository userActivityRecordRepository;

    @MockBean
    private UserDtoMapper userDtoMapper;

    @Test
    void contextLoads() {
    }
}
