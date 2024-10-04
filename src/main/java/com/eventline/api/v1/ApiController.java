package com.eventline.api.v1;

import com.eventline.shared.response.ResponseUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name="Test API", description = "To Test the server")
public class ApiController {

    @GetMapping("/health-check")
    public ResponseEntity<?> healthCheck() {
        return ResponseUtil.buildSuccessResponse("Server is working fine...", null);
    }
}
