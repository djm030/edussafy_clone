package com.edussafy.clone.domain.user.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.entity.UserStat;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.domain.repository.UserRepository;
import com.edussafy.clone.domain.user.domain.repository.UserStatRepository;
import com.edussafy.clone.domain.user.dto.mapper.UserDtoMapper;
import com.edussafy.clone.domain.user.dto.response.CampusSummaryResponse;
import com.edussafy.clone.domain.user.exception.UserNotFoundException;
import org.mapstruct.factory.Mappers;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserStatRepository userStatRepository;

    private final UserDtoMapper userDtoMapper = Mappers.getMapper(UserDtoMapper.class);

    @Test
    void getCampusSummary_returns_user_profile_and_stats() {
        User user = user();
        UserStat stat = UserStat.builder()
                .user(user)
                .scholarshipPoint(120)
                .totalExp(3400)
                .levelName("Lv.3 성장하는 개발자")
                .levelNo(3)
                .attendanceRate(96.5)
                .completedLearningCount(12)
                .build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userStatRepository.findByUser(user)).willReturn(Optional.of(stat));
        UserQueryService service = new UserQueryService(userRepository, userStatRepository, userDtoMapper);

        CampusSummaryResponse response = service.getCampusSummary(1L);

        assertThat(response.user().email()).isEqualTo("user@example.com");
        assertThat(response.scholarshipPoint()).isEqualTo(120);
        assertThat(response.totalExp()).isEqualTo(3400);
        assertThat(response.levelName()).isEqualTo("Lv.3 성장하는 개발자");
        assertThat(response.levelNo()).isEqualTo(3);
        assertThat(response.attendanceRate()).isEqualTo(96.5);
        assertThat(response.completedLearningCount()).isEqualTo(12);
        assertThat(response.unreadNotificationCount()).isZero();
    }

    @Test
    void getCampusSummary_returns_zero_defaults_when_user_stat_does_not_exist_yet() {
        User user = user();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userStatRepository.findByUser(user)).willReturn(Optional.empty());
        UserQueryService service = new UserQueryService(userRepository, userStatRepository, userDtoMapper);

        CampusSummaryResponse response = service.getCampusSummary(1L);

        assertThat(response.user().email()).isEqualTo("user@example.com");
        assertThat(response.scholarshipPoint()).isZero();
        assertThat(response.totalExp()).isZero();
        assertThat(response.levelName()).isEqualTo("Lv.1");
        assertThat(response.levelNo()).isEqualTo(1);
        assertThat(response.attendanceRate()).isEqualTo(0.0);
        assertThat(response.completedLearningCount()).isZero();
    }

    @Test
    void getCampusSummary_throws_when_user_not_found() {
        given(userRepository.findById(999L)).willReturn(Optional.empty());
        UserQueryService service = new UserQueryService(userRepository, userStatRepository, userDtoMapper);

        assertThatThrownBy(() -> service.getCampusSummary(999L))
                .isInstanceOf(UserNotFoundException.class);
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded-password")
                .name("홍길동")
                .studentNo("1234567")
                .generation(12)
                .region("서울")
                .classNo(1)
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();
    }
}
