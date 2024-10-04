package com.eventline.api.v1.OTP;

public class VerificationConstants {

    public static final int maxRequestsPerMinute = 5;
    public static final int maxFailedAttempts = 3;
    public static final int otpExpirationMinutes = 10;
    public static final int resendIntervalSeconds = 2; // Resend interval of 2 seconds
    public static final int lockDurationSeconds = 60; // Lock account for 1 hour
    public static final int defaultOtpLength = 6;

}
