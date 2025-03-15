package com.agribridge.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

public class RegistrationVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;

    Date expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public RegistrationVerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = calculateExpirationTime();
    }

    public RegistrationVerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationTime();
    }

    private Date calculateExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, 10);
        return new Date(calendar.getTime().getTime());
    }
}
