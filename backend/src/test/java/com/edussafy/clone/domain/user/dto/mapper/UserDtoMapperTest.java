package com.edussafy.clone.domain.user.dto.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.user.domain.entity.User;
import com.edussafy.clone.domain.user.domain.enums.UserRole;
import com.edussafy.clone.domain.user.domain.enums.UserStatus;
import com.edussafy.clone.domain.user.dto.response.UserMeResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class UserDtoMapperTest {

    private final UserDtoMapper mapper = Mappers.getMapper(UserDtoMapper.class);

    @Test
    void maps_user_entity_to_me_response() {
        User user = User.builder()
                .id(1L)
                .email("user@example.com")
                .password("encoded")
                .name("홍길동")
                .studentNo("1234567")
                .generation(12)
                .region("서울")
                .classNo(1)
                .role(UserRole.STUDENT)
                .status(UserStatus.ACTIVE)
                .build();

        UserMeResponse response = mapper.toMeResponse(user);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.email()).isEqualTo("user@example.com");
        assertThat(response.name()).isEqualTo("홍길동");
        assertThat(response.role()).isEqualTo(UserRole.STUDENT);
        assertThat(response.status()).isEqualTo(UserStatus.ACTIVE);
    }
}
