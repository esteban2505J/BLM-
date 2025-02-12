package com.microservice.user.presentation.controller;

import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.LoginDTO;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.TokenDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.implementation.AuthServiceImpl;
import com.microservice.user.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.nimbus.State;

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

    @PostMapping("/forgotPassword")
    public ResponseEntity<ResponseDTO<String>> forgotPassword(@Valid @RequestBody String email){
        if (authServiceImpl.forgotPassword(email) != StateRequest.SUCCESS) {
            return ResponseEntity.badRequest().body(new ResponseDTO<>(StateRequest.ERROR, "some went wrong"));
        }
        return ResponseEntity.ok().body(new ResponseDTO<>(StateRequest.SUCCESS, "email was send to your email"));

    }

    @PostMapping("/checkTokenPassword")
    public ResponseEntity<ResponseDTO> verifyTokenPassword(@Valid @RequestBody TokenDTO tokenDTO, @Valid @RequestBody String email) {

        StateRequest isValid =  authServiceImpl.checkTokenPassword(tokenDTO, email);
        if(isValid != StateRequest.SUCCESS) {
            return ResponseEntity.badRequest().body(new ResponseDTO<>(isValid, false));
        }
        return ResponseEntity.ok().body(new ResponseDTO<>(StateRequest.SUCCESS, true));

    }

    @PostMapping("resetPassword")
    public ResponseEntity<ResponseDTO> changePassword(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            StateRequest isPasswordChanged = authServiceImpl.resetPassword(loginDTO);
            if(isPasswordChanged != StateRequest.SUCCESS) {
                return ResponseEntity.badRequest().body(new ResponseDTO<>(isPasswordChanged, false));
            }
            return ResponseEntity.ok().body(new ResponseDTO<>(StateRequest.SUCCESS, true));

        }catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDTO(StateRequest.ERROR, e.getMessage()));
        }

    }





}
