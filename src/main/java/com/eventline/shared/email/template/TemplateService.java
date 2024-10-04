package com.eventline.shared.email.template;

import java.util.Map;

public interface TemplateService {
    String generateTemplateContent(String templateName, Map<String, Object> variables);
}

