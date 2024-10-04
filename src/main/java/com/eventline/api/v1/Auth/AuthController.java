package com.eventline.api.v1.Auth;

import com.eventline.api.v1.Auth.DTO.*;
import com.eventline.shared.constants.Messages;
import com.eventline.shared.response.ApiSuccessResponse;
import com.eventline.shared.response.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth Module", description = "APIs related to authentication")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Sign up a new user", description = "Register a new user using email and password.")
    @PostMapping("/signup")
    public ResponseEntity<ApiSuccessResponse<SignupResponseDto>> signUp(@Valid @RequestBody SignupDto signupDto) {
        SignupResponseDto response = authService.registerUser(signupDto);
        return ResponseUtil.buildSuccessResponse(Messages.REGISTER_SUCCESS, response);
    }


    @Operation(summary = "Login via email and password")
    @PostMapping("/login_via_email_password")
    public ResponseEntity<ApiSuccessResponse<LoginResponseDto>> loginViaEmailAndPassword(@Valid @RequestBody LoginDto loginDto) {
        LoginResponseDto response = authService.loginViaEmailAndPassword(loginDto);
        return ResponseUtil.buildSuccessResponse(Messages.LOGIN_SUCCESS, response);
    }


    @Operation(summary = "Request OTP for password reset", description = "Send OTP to user's email for password reset")
    @PostMapping("/password-reset/request")
    public ResponseEntity<ApiSuccessResponse<Object>> requestPasswordReset(@Valid @RequestBody RequestPasswordResetDto requestPasswordResetDto) {
        authService.requestPasswordReset(requestPasswordResetDto);
        return ResponseUtil.buildSuccessResponse(Messages.formatMessage(Messages.OTP_SENT, requestPasswordResetDto.getEmail()), null);
    }


    @Operation(summary = "Reset password using OTP", description = "Reset user's password after OTP verification.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/password-reset/reset")
    public ResponseEntity<ApiSuccessResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {

        return ResponseUtil.buildSuccessResponse(Messages.RESET_SUCCESS, null);
    }

}
