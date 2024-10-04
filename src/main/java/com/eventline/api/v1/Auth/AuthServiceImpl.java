package com.eventline.api.v1.Auth;

import com.eventline.api.v1.Auth.DTO.*;
import com.eventline.api.v1.User.DAO.UserDao;
import com.eventline.api.v1.User.model.User;
import com.eventline.api.v1.OTP.NotificationAction;
import com.eventline.api.v1.OTP.NotificationChannel;
import com.eventline.api.v1.OTP.service.OtpService;
import com.eventline.jwt.JwtUtils;
import com.eventline.shared.constants.AppEnums;
import com.eventline.shared.constants.Messages;
import com.eventline.shared.email.service.EmailService;
import com.eventline.shared.exceptions.BadInputException;
import com.eventline.shared.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final OtpService verificationService;

    @Autowired
    public AuthServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager, EmailService emailService, OtpService verificationService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.verificationService = verificationService;
    }

    @Override
    public SignupResponseDto registerUser(SignupDto payload) throws BadInputException {

        Optional<User> existingUser = userDao.findByEmail(payload.getEmail());

        if (existingUser.isPresent()) {
            throw new BadInputException("Email is already registered", "email");
        }

        User user = User.builder()
                .fullName(payload.getFullName())
                .email(payload.getEmail().toLowerCase())
                .password(passwordEncoder.encode(payload.getPassword()))
                .loginType(AppEnums.LoginType.NORMAL.getValue())
                .isEnabled(true)
                .roles(List.of(AppEnums.Role.USER))
                .lastLogin(Instant.now())
                .expirationDate(Instant.now().plusMillis(AppEnums.TTL.PWD_EXPIRATION))
                .build();

        User savedUser = userDao.save(user);
        return new SignupResponseDto(savedUser.getUsername());
    }


    private LoginResponseDto generateLoginResponse(User user) {
        String subject = user.getUsername();
        String accessToken = jwtUtils.generateAccessToken(subject);
        String refreshToken = jwtUtils.generateRefreshToken(subject);
        return LoginResponseDto.builder()
                .userId(subject)
                .email(user.getEmail())
                .loginType(user.getLoginType())
                .isEnabled(user.isEnabled())
                .roles(user.getRoles())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public LoginResponseDto loginViaEmailAndPassword(LoginDto loginDto) throws BadInputException {
        User existingUser = userDao.findByEmail(loginDto.getEmail()).orElseThrow(() -> new BadInputException(Messages.EMAIL_NOT_REGISTERED));
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())); // Authenticate the user using email and password
        SecurityContextHolder.getContext().setAuthentication(authentication);
        LoginResponseDto loginResponse = this.generateLoginResponse((User) authentication.getPrincipal());
        existingUser.setLastLogin(Instant.now());
        userDao.save(existingUser);
        return loginResponse;
    }

    @Override
    public void requestPasswordReset(RequestPasswordResetDto requestPasswordResetDto) throws BadInputException {
        User isUserExist = userDao.findByEmail(requestPasswordResetDto.getEmail()).orElseThrow(() -> new BadInputException(Messages.EMAIL_NOT_REGISTERED));
        if (!isUserExist.isEnabled()) throw new UnauthorizedException(Messages.BLOCKED);


        Map<String, Object> variables = new HashMap<>();
        variables.put("name", isUserExist.getFullName());
        verificationService.sendOtp(requestPasswordResetDto.getEmail(), variables, NotificationChannel.EMAIL, NotificationAction.FORGOT_PASSWORD);

    }
}
