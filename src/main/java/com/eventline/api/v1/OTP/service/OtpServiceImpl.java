package com.eventline.api.v1.OTP.service;

import com.eventline.api.v1.OTP.DAO.OtpRecordDao;
import com.eventline.api.v1.OTP.NotificationAction;
import com.eventline.api.v1.OTP.NotificationChannel;
import com.eventline.api.v1.OTP.OtpRecord;
import com.eventline.api.v1.OTP.VerificationConstants;
import com.eventline.shared.email.model.EmailRequest;
import com.eventline.shared.email.service.EmailService;
import com.eventline.shared.exceptions.BadInputException;
import com.eventline.shared.exceptions.TooEarlyRequestException;
import com.eventline.shared.exceptions.TooManyReqeustsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpServiceImpl implements OtpService {
    private final OtpRecordDao otpRecordDao;
    private final EmailService emailService;
    private final Random random;

    @Autowired
    public OtpServiceImpl(OtpRecordDao otpRecordDao, EmailService emailService) {
        this.otpRecordDao = otpRecordDao;
        this.emailService = emailService;
        this.random = new Random();
    }

    @Override
    public void sendOtp(String recipient, Map<String, Object> contents, NotificationChannel channel, NotificationAction action) {
        OtpRecord otpRecord = checkRateLimitingAndLocking(recipient);

        if (isWithinResendInterval(otpRecord)) {
            throw new TooEarlyRequestException("Frequent sending of requests. Please wait " + VerificationConstants.resendIntervalSeconds + " seconds before trying again.");
        }

        String otp = generateOtp();
        String hashedOtp = hashOtp(otp);
        saveOrUpdateOtpRecord(recipient, hashedOtp, otpRecord);
        notifyRecipient(recipient, otp, contents, channel, action);
    }

    @Override
    public boolean verifyOtp(String recipient, String otp) {
        Optional<OtpRecord> otpRecordOpt = otpRecordDao.findByRecipient(recipient);
        if (otpRecordOpt.isEmpty()) return false;

        OtpRecord otpRecord = otpRecordOpt.get();

        if (isOtpExpired(otpRecord)) {
            otpRecordDao.delete(otpRecord); // Remove expired OTP
            throw new BadInputException("OTP has expired.");
        }

        // Check if max failed attempts are exceeded
        if (otpRecord.getFailedAttempts() >= VerificationConstants.maxFailedAttempts) {
            lockAccount(otpRecord); // Lock the account for 1 hour
            throw new TooManyReqeustsException("Account is locked. Please try again after " + otpRecord.getLockUntil());
        }


        if (BCrypt.checkpw(otp, otpRecord.getHashedOtp())) {
            otpRecordDao.delete(otpRecord); // Remove OTP after successful verification
            return true;
        } else {
            // Increment failed attempts
            otpRecord.setFailedAttempts(otpRecord.getFailedAttempts() + 1);
            otpRecordDao.save(otpRecord);
            return false;
        }


    }

    private OtpRecord checkRateLimitingAndLocking(String recipient) {
        Optional<OtpRecord> otpRecordOpt = otpRecordDao.findByRecipient(recipient);
        OtpRecord otpRecord = otpRecordOpt.orElse(new OtpRecord()); // Use existing or create a new one

        if (isAccountLocked(otpRecord)) {
            throw new TooManyReqeustsException("Account is locked. Please try again after " + otpRecord.getLockUntil());
        }

        if (otpRecord.getRequestCount() >= VerificationConstants.maxRequestsPerMinute) {
            lockAccount(otpRecord);
            throw new TooManyReqeustsException("Too many requests. Your account is now locked for 1 hour.");
        }

        // Increment request count for existing record or set to 1 for a new record
        otpRecord.setRequestCount(otpRecord.getRequestCount() + 1);
        return otpRecord;
    }

    private boolean isAccountLocked(OtpRecord otpRecord) {
        return otpRecord.getLockUntil() != null && otpRecord.getLockUntil().isAfter(Instant.now());
    }

    private boolean isWithinResendInterval(OtpRecord otpRecord) {
        return otpRecord.getCreatedAt() != null && Instant.now().isBefore(otpRecord.getCreatedAt().plusSeconds(VerificationConstants.resendIntervalSeconds));
    }

    private boolean isOtpExpired(OtpRecord otpRecord) {
        return otpRecord.getExpiresAt().isBefore(Instant.now());
    }

    private void lockAccount(OtpRecord otpRecord) {
        otpRecord.setLockUntil(Instant.now().plusSeconds(VerificationConstants.lockDurationSeconds)); // Lock account for 1 hour
        otpRecordDao.save(otpRecord);
    }

    private String generateOtp() {
        int minLimit = (int) Math.pow(10, VerificationConstants.defaultOtpLength - 1);
        int maxLimit = (int) Math.pow(10, VerificationConstants.defaultOtpLength) - 1;
        return String.valueOf(random.nextInt(maxLimit - minLimit + 1) + minLimit);
    }

    private String hashOtp(String otp) {
        return BCrypt.hashpw(otp, BCrypt.gensalt());
    }


    private void saveOrUpdateOtpRecord(String recipient, String hashedOtp, OtpRecord existingRecord) {
        OtpRecord otpRecord = existingRecord != null ? existingRecord : new OtpRecord();
        otpRecord.setRecipient(recipient);
        otpRecord.setHashedOtp(hashedOtp);
        otpRecord.setCreatedAt(Instant.now());
        otpRecord.setExpiresAt(Instant.now().plusSeconds(VerificationConstants.otpExpirationMinutes * 60));

        otpRecordDao.save(otpRecord); // Save updated or new record
    }

    private void notifyRecipient(String recipient, String otp, Map<String, Object> contents, NotificationChannel channel, NotificationAction action) {
        if (channel == NotificationChannel.SMS) {
            sendSms(recipient, otp);
        } else if (channel == NotificationChannel.EMAIL) {
            contents.put("otp", otp);
            sendEmail(recipient, contents, action);
        } else {
            throw new IllegalArgumentException("Unsupported notification channel: " + channel);
        }
    }

    private void sendEmail(String recipient, Map<String, Object> variables, NotificationAction action) {
        EmailRequest emailRequest = EmailRequest.builder()
                .to(recipient)
                .subject(getEmailSubject(action))
                .variables(variables)
                .templateName(getEmailTemplate(action))
                .build();

        emailService.sendEmail(emailRequest);
    }

    private void sendSms(String recipient, String otp) {
        // Implement SMS sending logic here
        System.out.println("Send OTP " + otp + " to: " + recipient);
    }

    private String getEmailSubject(NotificationAction action) {
        return switch (action) {
            case RESEND_CODE -> "Resend OTP";
            case FORGOT_PASSWORD -> "Forgot Password OTP";
            case VERIFY_EMAIL -> "Email Verification OTP";
            case VERIFY_PHONE -> "Phone Number Verification OTP";
        };
    }

    private String getEmailTemplate(NotificationAction action) {
        return switch (action) {
            case RESEND_CODE -> "resend-otp-email";
            case FORGOT_PASSWORD -> "reset-password-email";
            case VERIFY_EMAIL -> "welcome-email";
            case VERIFY_PHONE -> "verify-otp-email";
        };
    }
}
