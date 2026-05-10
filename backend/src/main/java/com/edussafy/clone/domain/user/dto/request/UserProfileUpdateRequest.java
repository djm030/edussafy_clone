package com.edussafy.clone.domain.user.dto.request;

import com.edussafy.clone.domain.user.application.command.UpdateUserProfileCommand;
import jakarta.validation.constraints.Size;

public record UserProfileUpdateRequest(
        @Size(max = 30, message = "휴대폰 번호는 30자를 넘을 수 없습니다.") String phoneNumber,
        @Size(max = 30, message = "긴급연락처는 30자를 넘을 수 없습니다.") String emergencyPhoneNumber,
        @Size(max = 20, message = "우편번호는 20자를 넘을 수 없습니다.") String zipCode,
        @Size(max = 255, message = "주소는 255자를 넘을 수 없습니다.") String address,
        @Size(max = 255, message = "상세주소는 255자를 넘을 수 없습니다.") String addressDetail
) {
    public UpdateUserProfileCommand toCommand() {
        return new UpdateUserProfileCommand(phoneNumber, emergencyPhoneNumber, zipCode, address, addressDetail);
    }
}
