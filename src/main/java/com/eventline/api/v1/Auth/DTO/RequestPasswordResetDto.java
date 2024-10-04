package com.eventline.api.v1.Auth.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestPasswordResetDto {

    @Schema(description = "Email address of the user requesting password reset", example = "user@example.com")
    @Email(message = "Must be a valid email address")
    @NotBlank(message = "Email is required")
    private String email;
}
