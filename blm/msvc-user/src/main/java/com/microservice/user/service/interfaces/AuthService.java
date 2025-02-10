package com.microservice.user.service.interfaces;

import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public interface AuthService {

    public TokenDTO register(@RequestBody UserDTO user);

    public TokenDTO login(@RequestBody LoginDTO loginDTO);

    public boolean checkToken(@RequestBody @Valid TokenDTO token);

    public StateRequest forgotPassword(@RequestParam String email);

}
