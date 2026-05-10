package com.edussafy.clone.global.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class BusinessExceptionTest {

    @Test
    void business_exception_exposes_error_code_status_and_message() {
        BusinessException exception = new BusinessException(ErrorCode.USER_NOT_FOUND);

        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(exception.getMessage()).isEqualTo("사용자를 찾을 수 없습니다.");
    }
}
