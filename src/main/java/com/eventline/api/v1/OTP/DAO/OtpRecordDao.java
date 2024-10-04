package com.eventline.api.v1.OTP.DAO;

import com.eventline.api.v1.OTP.OtpRecord;

import java.util.Optional;

public interface OtpRecordDao {
    void save(OtpRecord otpRecord);

    Optional<OtpRecord> findByRecipient(String recipient);

    void delete(OtpRecord otpRecord);
}
