package com.eventline.shared.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiSuccessResponse<T> {

    private int status;
    private String message;
    private T data;
}
