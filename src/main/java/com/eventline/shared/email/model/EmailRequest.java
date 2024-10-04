package com.eventline.shared.email.model;


import lombok.Builder;

import java.util.Map;

@Builder
public record EmailRequest(
        String to,
        String subject,
        Map<String, Object> variables,
        String templateName
) {
}
