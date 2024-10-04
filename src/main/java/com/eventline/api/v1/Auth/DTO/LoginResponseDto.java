package com.eventline.api.v1.Auth.DTO;

import com.eventline.shared.constants.AppEnums;
import lombok.Builder;

import java.util.List;

@Builder
public record LoginResponseDto(
        String userId,
        String email,
        Boolean isEnabled,
        String loginType,
        List<AppEnums.Role> roles,
        String accessToken,
        String refreshToken
) {
}
