package com.microservice.user.presentation.controller;

import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @RequestMapping("/register")
    public ResponseEntity<ResponseDTO> createUser(@Valid  @RequestBody UserDTO user) {


        return ResponseEntity.ok().body(new ResponseDTO());

    }



}
