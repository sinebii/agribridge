package com.agribridge.dto;

import com.agribridge.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JsonResponse {
    private Object data;
    private String message;
    private HttpStatus status;
    private Integer statusCode;
    private User user;

}
