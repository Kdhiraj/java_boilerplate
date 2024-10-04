
package com.eventline.shared.email.service;

import com.eventline.shared.email.model.EmailRequest;

public interface EmailService {
    void sendEmail(EmailRequest emailRequest);
}
