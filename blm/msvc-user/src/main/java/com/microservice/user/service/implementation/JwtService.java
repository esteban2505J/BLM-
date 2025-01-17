package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    @Value("${KEY_SECRET}")
    private String secretKey;

    @Value("${JWT_EXPIRATION}")
    private long jwtExpirationTime;

    @Value("${REFRESH_EXPIRATION}")
    private long refreshExpirationTime;

    public String generateToken(UserEntity user) {
        return buildToken(user, jwtExpirationTime);
    }

    public String generateRefreshToken(UserEntity user) {
        return buildToken(user, refreshExpirationTime);
    }


    private String buildToken(UserEntity user, long expirationTime) {

        return Jwts.builder()
                .id(user.getUserId())
                .claims(Map.of("name", user.getFirstName()))
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey())
                .compact();


    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
