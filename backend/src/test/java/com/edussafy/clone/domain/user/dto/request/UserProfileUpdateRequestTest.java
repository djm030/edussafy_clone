package com.edussafy.clone.domain.user.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import com.edussafy.clone.domain.user.application.command.UpdateUserProfileCommand;
import org.junit.jupiter.api.Test;

class UserProfileUpdateRequestTest {

    @Test
    void request_converts_to_command_without_entity_dependency() {
        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
                "010-0000-0000",
                "010-1111-1111",
                "06234",
                "서울시 강남구",
                "101호"
        );

        UpdateUserProfileCommand command = request.toCommand();

        assertThat(command.phoneNumber()).isEqualTo("010-0000-0000");
        assertThat(command.emergencyPhoneNumber()).isEqualTo("010-1111-1111");
        assertThat(command.zipCode()).isEqualTo("06234");
        assertThat(command.address()).isEqualTo("서울시 강남구");
        assertThat(command.addressDetail()).isEqualTo("101호");
    }
}
