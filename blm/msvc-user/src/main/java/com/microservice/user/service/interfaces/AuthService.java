package com.microservice.user.service.interfaces;

import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
public interface AuthService {

    public TokenDTO register(@RequestBody UserDTO user);

    public TokenDTO login(@RequestBody LoginDTO loginDTO);
    public TokenDTO refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);
}
