package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.vo.TokenEntity;
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

    public TokenEntity generateToken(UserEntity user) {

        if(user == null || user.getId() == null) {throw  new IllegalArgumentException("Invalid user");}
        return   TokenEntity.builder().user(user).token(buildToken(user,jwtExpirationTime)).build();
    }

    public TokenEntity generateRefreshToken(UserEntity user) {

        return TokenEntity.builder().user(user).token(buildToken(user,refreshExpirationTime)).build();
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
