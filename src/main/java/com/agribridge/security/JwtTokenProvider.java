package com.agribridge.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider  {
    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwt-expiration-millisecons}")
    private long jwtExpirationInMs;

    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        System.err.println(authentication);
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(key())
                .compact();

        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret)
         );
    }

    public String getUsername(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Boolean validateToken(String token){
        Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parse(token);

        return true;
    }
}
