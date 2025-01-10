package com.microservice.user.service.interfaces;


import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    public ResponseEntity<TokenDTO> createUser(UserDTO userDTO);
    public ResponseEntity<ResponseDTO> updateUser(UserDTO userDTO);
}