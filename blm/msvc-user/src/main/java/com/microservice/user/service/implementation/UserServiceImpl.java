package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.UserEntity;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseEntity<TokenDTO> createUser(UserDTO userDTO) {

        if (userDTO == null) new IllegalArgumentException("userDTO is null");


        UserEntity newUserEntity = new UserEntity();
        newUserEntity.setFirstName(userDTO.firstName());
        newUserEntity.setLastName(userDTO.lastName());
        newUserEntity.setEmail(userDTO.email());
        newUserEntity.setPassword(passwordEncoder.encode(userDTO.password()));

        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> loginUser(LoginDTO loginDTO) {
        return null;
    }
}
