package com.microservice.user.service.implementation;

import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import org.springframework.http.ResponseEntity;

public class AuthServiceImpl implements AuthService {
    @Override
    public ResponseEntity<TokenDTO> register(UserDTO user) {
        return null;
    }

    @Override
    public ResponseEntity<TokenDTO> login(LoginDTO loginDTO) {
        return null;
    }

    @Override
    public TokenDTO refreshToken(String token) {
        return null;
    }
}
