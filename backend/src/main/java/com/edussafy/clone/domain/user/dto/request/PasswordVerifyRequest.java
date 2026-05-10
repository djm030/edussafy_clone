package com.edussafy.clone.domain.user.dto.request;

import com.edussafy.clone.domain.user.application.command.VerifyPasswordCommand;
import jakarta.validation.constraints.NotBlank;

public record PasswordVerifyRequest(@NotBlank(message = "비밀번호를 입력하세요.") String password) {
    public VerifyPasswordCommand toCommand() {
        return new VerifyPasswordCommand(password);
    }
}
