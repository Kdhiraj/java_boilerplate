package com.eventline.api.v1.OTP.DAO;


import com.eventline.api.v1.OTP.OtpRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OtpRecordDaoImpl implements OtpRecordDao {

    private final OtpRecordRepository otpRecordRepository;

    @Autowired
    public OtpRecordDaoImpl(OtpRecordRepository otpRecordRepository) {
        this.otpRecordRepository = otpRecordRepository;
    }

    @Override
    public void save(OtpRecord otpRecord) {
        otpRecordRepository.save(otpRecord);
    }

    @Override
    public Optional<OtpRecord> findByRecipient(String recipient) {
        return otpRecordRepository.findByRecipient(recipient);
    }

    @Override
    public void delete(OtpRecord otpRecord) {
        otpRecordRepository.delete(otpRecord);
    }
}
