package com.eventline.api.v1.OTP.service;


import com.eventline.api.v1.OTP.NotificationAction;
import com.eventline.api.v1.OTP.NotificationChannel;

import java.util.Map;

public interface OtpService {
    void sendOtp(String recipient, Map<String, Object> contents, NotificationChannel channel, NotificationAction action);
    boolean verifyOtp(String recipient, String otp);
}
