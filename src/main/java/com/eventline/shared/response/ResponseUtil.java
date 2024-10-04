package com.eventline.shared.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiSuccessResponse<T>> buildSuccessResponse(String message, T data) {
        ApiSuccessResponse<T> response = new ApiSuccessResponse<>(HttpStatus.OK.value(), message, data);
        return ResponseEntity.ok(response);
    }
}
