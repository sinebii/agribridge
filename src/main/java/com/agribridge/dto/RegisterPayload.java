package com.agribridge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPayload {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String gender;
    private LocalDateTime dateOfBirth;
    private String address;
    private Long roleId;
    private String email;
    private String password;
}
