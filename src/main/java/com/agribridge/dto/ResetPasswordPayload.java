package com.agribridge.dto;

import lombok.Data;

@Data
public class ResetPasswordPayload {
    private String password;
    private String newPassword;
}
