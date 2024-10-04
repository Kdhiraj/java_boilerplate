package com.eventline.api.v1.User.DTO;

import com.eventline.shared.constants.AppEnums;
import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record ProfileRecordDto(
        String userId,
        String fullName,
        String email,
        String profileImage,
        String loginType,
        String countryCode,
        String phoneNumber,
        List<AppEnums.Role> roles,
        Instant lastLogin,
        Instant createdAt,
        Instant expirationDate,
        Instant lastPasswordChangeDate,
        boolean emailVerified,
        boolean phoneVerified,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        boolean locked,
        boolean enabled
) {
}
