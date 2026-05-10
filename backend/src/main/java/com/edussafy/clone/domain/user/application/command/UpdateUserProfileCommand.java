package com.edussafy.clone.domain.user.application.command;

public record UpdateUserProfileCommand(
        String phoneNumber,
        String emergencyPhoneNumber,
        String zipCode,
        String address,
        String addressDetail
) {
}
