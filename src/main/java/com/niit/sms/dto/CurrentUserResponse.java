package com.niit.sms.dto;

public record CurrentUserResponse(
        String userId,
        String username,
        String email,
        String role,
        String profileId,
        String fullName
) {
}