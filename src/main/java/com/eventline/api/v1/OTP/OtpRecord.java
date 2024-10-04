
package com.eventline.api.v1.OTP;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "otp_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OtpRecord {

    @Id
    private String id;
    private String recipient; // Phone number or email
    private String hashedOtp;
    private Instant createdAt;
    private Instant expiresAt;
    private Instant lockUntil;
    private int failedAttempts;
    private int requestCount;


}
