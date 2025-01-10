package com.microservice.user.service.implementation;

import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;




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
