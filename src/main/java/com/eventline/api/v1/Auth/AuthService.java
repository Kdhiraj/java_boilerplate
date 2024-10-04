package com.eventline.api.v1.Auth;

import com.eventline.api.v1.Auth.DTO.*;
import com.eventline.shared.exceptions.BadInputException;

import javax.security.auth.login.AccountLockedException;

public interface AuthService {
    SignupResponseDto registerUser(SignupDto payload) throws BadInputException;

    LoginResponseDto loginViaEmailAndPassword(LoginDto payload) throws BadInputException;

    void requestPasswordReset(RequestPasswordResetDto requestPasswordResetDto) throws BadInputException;

}
