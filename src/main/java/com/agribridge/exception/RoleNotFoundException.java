package com.agribridge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class RoleNotFoundException extends RuntimeException{
    private HttpStatus httpStatus;
    private String message;
}
