package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.persitence.repository.UserRepository;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import com.microservice.user.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AppUtil appUtil;




    @Override
    public ResponseEntity<TokenDTO> register(UserDTO user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        try {
            if(appUtil.checkEmail(user.email())) throw new IllegalArgumentException("Email is already in use");
           UserEntity newUser =  UserEntity.builder()
                    .email(user.email())
                    .password(passwordEncoder.encode(user.password()))
                    .credentialsNonExpired(true)
                    .isEnabled(true)
                    .firstName(user.firstName())
                    .lastName(user.lastName())
                    .phoneNumber(user.phoneNumber()).build();

            return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(newUser)));


        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new TokenDTO(e.getMessage()));
        }

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
