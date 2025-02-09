package com.microservice.user.presentation.controller;

import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.implementation.AuthServiceImpl;
import com.microservice.user.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("permitAll()")
@RequestMapping("/auth")
public class AuthController {

    private final AuthServiceImpl authServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public AuthController(AuthServiceImpl authServiceImpl, UserServiceImpl userServiceImpl) {
        this.authServiceImpl = authServiceImpl;
        this.userServiceImpl = userServiceImpl;
    }


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<TokenDTO>> createUser(@Valid  @RequestBody UserDTO user) {
        try {
            TokenDTO token = authServiceImpl.register(user);
            if (token == null) {
                return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, "something went wrong"));

            }
                return ResponseEntity.ok().body(new ResponseDTO<TokenDTO>(StateRequest.SUCCESS, token));
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, e.getMessage()));
        }

    }

    @PostMapping("/login")
    public  ResponseEntity<ResponseDTO<TokenDTO>> login(@Valid @RequestBody LoginDTO loginDTO) {
        TokenDTO tokenLogin = authServiceImpl.login(loginDTO);
        if (tokenLogin == null) {
            return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, "something went wrong"));
        }
        return ResponseEntity.ok().body(new ResponseDTO<TokenDTO>(StateRequest.SUCCESS, tokenLogin));
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<ResponseDTO<Boolean>> verifyToken(@Valid @RequestBody TokenDTO tokenDTO) {
        if(tokenDTO == null || tokenDTO.token().isBlank()) {
            return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, "token is empty"));
        }
        boolean isValid = authServiceImpl.checkToken(tokenDTO);
        if(!isValid) {
            return ResponseEntity.badRequest().body(new ResponseDTO<>(StateRequest.ERROR, false));
        }
        return ResponseEntity.ok().body(new ResponseDTO<>(StateRequest.SUCCESS, true));


    }



}
