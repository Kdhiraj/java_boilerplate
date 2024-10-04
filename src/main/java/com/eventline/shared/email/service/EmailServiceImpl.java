package com.eventline.shared.email.service;

import com.eventline.shared.email.model.EmailRequest;
import com.eventline.shared.email.template.TemplateService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateService templateService;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);


    public EmailServiceImpl(JavaMailSender mailSender, TemplateService templateService) {
        this.mailSender = mailSender;
        this.templateService = templateService;
    }

    @Override
    @Async  // Mark this method as asynchronous
    public void sendEmail(EmailRequest emailRequest) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(emailRequest.to());
            helper.setSubject(emailRequest.subject());

            // Generate email content using the template service
            String body = templateService.generateTemplateContent(
                    emailRequest.templateName(), emailRequest.variables()
            );
            helper.setText(body, true);  // Enable HTML
            mailSender.send(message);
            // Log after the mail has been sent
            logger.info("Email sent to: {}, Subject: {}", emailRequest.to(), emailRequest.subject());
        } catch (MessagingException e) {
            logger.error("Failed to create and send email", e);
        } catch (Exception e) {
            logger.error("An unexpected error occurred while sending email", e);
        }
    }
}
