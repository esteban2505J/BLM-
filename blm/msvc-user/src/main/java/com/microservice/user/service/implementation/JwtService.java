package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.model.vo.TokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
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

    public boolean validateToken(String token) {
        try {
            // Parseamos el token y extraemos sus claims
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(getSecretKey())  // Clave secreta que usamos para firmar el token
                    .build()
                    .parseClaimsJws(token);  // Esto validará la firma y la estructura del token

            // Comprobamos si el token ha expirado
            Date expirationDate = claims.getBody().getExpiration();
            if (expirationDate.before(new Date())) {
                // Si la fecha de expiración ya pasó
                return false;
            }

            // Si la fecha de expiración es válida, el token es válido
            return true;
        }catch (JwtException e) {
                return false;
        }
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
