package com.eventline.api.v1.OTP.DAO;

import com.eventline.api.v1.OTP.OtpRecord;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRecordRepository extends MongoRepository<OtpRecord, ObjectId> {
    Optional<OtpRecord> findByRecipient(String recipient);
}
