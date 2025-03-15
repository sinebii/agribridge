package com.agribridge.dto;

import com.agribridge.model.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class UserResponse {
    private String token;
    private String message;
    private HttpStatus status;
    private Integer statusCode;
    private User user;

}
