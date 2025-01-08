package com.microservice.user.service.implementation;

import com.microservice.user.persitence.model.entities.User;
import com.microservice.user.persitence.model.enums.Role;
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


        User newUser = new User();
        newUser.setFirstName(userDTO.firstName());
        newUser.setLastName(userDTO.lastName());
        newUser.setEmail(userDTO.email());
        newUser.setPassword(passwordEncoder.encode(userDTO.password()));

        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> loginUser(LoginDTO loginDTO) {
        return null;
    }
}
