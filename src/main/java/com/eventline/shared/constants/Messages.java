package com.eventline.shared.constants;

import java.text.MessageFormat;

public class Messages {

    public static final String USER_NOT_FOUND = "User not found";
    public static final String SUCCESS = "Success";
    public static final String EMAIL_ALREADY_REGISTERED = "Email is already registered";
    public static final String EMAIL_NOT_REGISTERED= "Email is not registered";
    public static final String AUTHENTICATION_FAILED = "Authentication failed. Please try again.";
    public static final String INVALID_INPUT = "Invalid input provided.";

    public static final String BLOCKED = "You are blocked by admin. Please contact admin.";
    public static final String REGISTER_SUCCESS = "Registered Successfully";

    public static final String LOGIN_SUCCESS = "LoggedIn Successfully";
    public static final String RESET_SUCCESS = "Password has been reset successfully";
    public static final String OTP_SENT = "OTP has been sent to {0}";

    // Add more messages here
    public static String formatMessage(String message, Object... args) {
        return MessageFormat.format(message, args);
    }
    private Messages() {
        // Private constructor to prevent instantiation
    }
}
