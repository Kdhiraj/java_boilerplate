package com.eventline.api.v1.Auth.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPasswordDto {

    @Schema(description = "Email address of the user", example = "user@example.com")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "The OTP sent to the user's email", example = "123456")
    @NotBlank(message = "OTP is required")
    private String otp;

    @Schema(description = "New password to be set", example = "strong_password@123")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String newPassword;
}
