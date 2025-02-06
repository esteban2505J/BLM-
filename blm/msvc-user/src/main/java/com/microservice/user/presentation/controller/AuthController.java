package com.microservice.user.presentation.controller;

import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.interfaces.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> createUser(@Valid  @RequestBody UserDTO user) {
        try {
            TokenDTO token = authService.register(user);
            if (token == null) {
                return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, "something went wrong"));

            }
                return ResponseEntity.ok().body(new ResponseDTO(StateRequest.SUCCESS, token));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, e.getMessage()));
        }

    }

    @PostMapping("/login")
    public  ResponseEntity<ResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
        return null;
    }



}
