package com.eventline.shared.exceptions;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiErrorResponse {
    private int status;
    private String message;
    private List<FieldError> reasons;

    /**
     * Inner class to represent individual field-level errors in validation.
     */
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldError {
        private String field;          // The name of the field that caused the error
        private String message;        // The error message for that field
    }
}
