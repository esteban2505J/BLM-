package com.microservice.user.presentation.controller;


import com.microservice.user.persitence.model.enums.StateRequest;
import com.microservice.user.presentation.dtos.ResponseDTO;
import com.microservice.user.presentation.dtos.UserDTO;
import com.microservice.user.service.implementation.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/userController")
@PreAuthorize("denyAll()")
public class UserController {

    UserServiceImpl userService;


    @PutMapping("/updateUser")
    public ResponseEntity<ResponseDTO<StateRequest>> updateUserController(@Valid @RequestBody UserDTO userDTO) {
        ResponseDTO<StateRequest> responseDTO = userService.updateUser(userDTO);
        if (responseDTO.stateRequest() == StateRequest.SUCCESS) {
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.status(400).body(responseDTO);

    }
}
