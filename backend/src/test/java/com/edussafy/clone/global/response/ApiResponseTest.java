package com.edussafy.clone.global.response;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void ok_wraps_success_data_and_default_message() {
        ApiResponse<String> response = ApiResponse.ok("hello");

        assertThat(response.success()).isTrue();
        assertThat(response.data()).isEqualTo("hello");
        assertThat(response.message()).isEqualTo("요청이 성공했습니다.");
    }

    @Test
    void fail_wraps_error_code_and_message_without_data() {
        ApiResponse<Void> response = ApiResponse.fail("USER_NOT_FOUND", "사용자를 찾을 수 없습니다.");

        assertThat(response.success()).isFalse();
        assertThat(response.data()).isNull();
        assertThat(response.errorCode()).isEqualTo("USER_NOT_FOUND");
        assertThat(response.message()).isEqualTo("사용자를 찾을 수 없습니다.");
    }
}
